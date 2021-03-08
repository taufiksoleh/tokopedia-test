package com.tokopedia.filter.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.slider.RangeSlider
import com.tokopedia.core.CacheUtils
import com.tokopedia.core.Constants.CITY
import com.tokopedia.core.Constants.PRICE_MAX
import com.tokopedia.core.Constants.PRICE_MIN
import com.tokopedia.core.Resource
import com.tokopedia.core.toCurrency
import com.tokopedia.filter.R
import com.tokopedia.filter.data.local.ProductRepository
import com.tokopedia.filter.data.models.City
import com.tokopedia.filter.viewmodel.ProductViewModel
import com.tokopedia.filter.viewmodel.ProductViewModelFactory


class FilterFragment : BottomSheetDialogFragment() {
    // view model
    private lateinit var mViewModel: ProductViewModel

    // ui component
    private lateinit var mRangeSlider: RangeSlider
    private lateinit var mCityGroup: ChipGroup
    private lateinit var mPriceMinText: TextView
    private lateinit var mPriceMaxText: TextView
    private lateinit var mFilterBtn: MaterialButton
    private lateinit var mResetBtn: MaterialButton

    // repository
    private lateinit var mProductRepository: ProductRepository

    // data
    private var mCity = ""
    private var mPriceMax: Float = 0f
    private var mPriceMin: Float = 0f

    // interface for passing data filter
    private lateinit var dataFiltered: OnDataFiltered

    // cache
    private lateinit var mCache: CacheUtils

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataFiltered = context as OnDataFiltered
    }

    private fun filterData(city: String, priceMin: Float, priceMax: Float) {

        // save data in cache
        mCache.setString(CITY, city)
        mCache.setFloat(PRICE_MIN, priceMin)
        mCache.setFloat(PRICE_MAX, priceMax)

        // passing data filter
        dataFiltered.onDataFiltered(city, priceMin, priceMax)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCache()
        initViewModel()
        initComponent(view)
        observe()
    }

    //initialize ui component
    private fun initComponent(view: View) {
        mRangeSlider = view.findViewById(R.id.range_slider)
        mRangeSlider.addOnChangeListener { slider, _, _ ->
            mPriceMin = slider.values[0]
            mPriceMax = slider.values[1]
        }
        mRangeSlider.setLabelFormatter { value ->
            value.toInt().toCurrency()
        }

        mCityGroup = view.findViewById(R.id.city_group)
        mCityGroup.isSingleSelection = true

        mPriceMaxText = view.findViewById(R.id.price_max)
        mPriceMinText = view.findViewById(R.id.price_min)

        mFilterBtn = view.findViewById(R.id.btn_filter)
        mFilterBtn.setOnClickListener {
            filterData(city = mCity, priceMin = mPriceMin, priceMax = mPriceMax)
            dismiss()
        }

        mResetBtn = view.findViewById(R.id.btn_reset)
        mResetBtn.setOnClickListener {
            mRangeSlider.apply {
                values = listOf(valueFrom, valueTo)

                mCity = ""
                mPriceMin = valueFrom
                mPriceMax = valueTo
            }

            filterData(city = mCity, priceMin = mPriceMin, priceMax = mPriceMax)
            dismiss()
        }
    }

    private fun initCache() {
        mCache = CacheUtils(requireContext())
    }

    @SuppressLint("WrongConstant")
    private fun addChip(data: List<City>?) {
        data?.forEachIndexed { index, city ->
            mCity = mCache.getString(CITY)!!
            val isSelected = mCity == city.name
            Log.e("isSelected", "$isSelected")
            val chip = Chip(requireContext())
            chip.apply {
                id = index
                text = city.name
                isCheckable = true
                isChecked = isSelected
            }

            mCityGroup.apply {
                addView(chip)
            }
        }

        mCityGroup.setOnCheckedChangeListener { group, id ->
            val chip: Chip = group.findViewById(id)
            mCity = chip.text.toString()
        }
    }

    private fun initDataFromCache(): Boolean {
        val min = mCache.getFloat(PRICE_MIN)
        val max = mCache.getFloat(PRICE_MAX)

        Log.e("initDataFromCache", "priceMin:$min priceMax:$max")
        if (min != 0.0f && max != 0.0f) {
            mRangeSlider.setValues(min, max)
            return true
        }

        return false
    }

    // initialize viewModel
    private fun initViewModel() {
        mProductRepository = ProductRepository(requireContext())
        val viewModelFactory = ProductViewModelFactory(mProductRepository)
        mViewModel = ViewModelProvider(viewModelStore, viewModelFactory)[ProductViewModel::class.java]
    }

    // observable data
    private fun observe() {
        mViewModel.getMostCity(limit = 3)
        mViewModel.observeCity().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    addChip(it.data)
                }
                is Resource.DataError -> {

                }
            }
        })

        mViewModel.getPrice()
        mViewModel.observePrice().observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    it.data?.let { price ->
                        mPriceMin = price.priceMin
                        mPriceMax = price.priceMax

                        mRangeSlider.valueFrom = mPriceMin
                        mRangeSlider.valueTo = mPriceMax
                        if (!initDataFromCache()) {
                            mRangeSlider.values = listOf(mPriceMin, mPriceMax)
                        }

                        mPriceMaxText.text = requireContext().getString(R.string.text_price).format(price.priceMax.toInt().toCurrency())
                        mPriceMinText.text = requireContext().getString(R.string.text_price).format(price.priceMin.toInt().toCurrency())
                    }
                }
                is Resource.DataError -> {

                }
            }
        })
    }

    override fun getTheme(): Int {
        return R.style.BottomSheet
    }

}