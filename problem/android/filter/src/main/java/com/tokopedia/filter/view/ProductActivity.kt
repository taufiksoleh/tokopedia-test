package com.tokopedia.filter.view

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tokopedia.core.CacheUtils
import com.tokopedia.core.Constants.CITY
import com.tokopedia.core.Constants.PRICE_MAX
import com.tokopedia.core.Constants.PRICE_MIN
import com.tokopedia.core.Resource
import com.tokopedia.filter.R
import com.tokopedia.filter.data.local.ProductRepository
import com.tokopedia.filter.data.models.Product
import com.tokopedia.filter.view.adapter.ProductAdapter
import com.tokopedia.filter.viewmodel.ProductViewModel
import com.tokopedia.filter.viewmodel.ProductViewModelFactory

class ProductActivity : AppCompatActivity(), OnDataFiltered {
    // view model
    private lateinit var viewModel: ProductViewModel

    // ui component
    private lateinit var productList: RecyclerView
    private lateinit var fabFilter: ExtendedFloatingActionButton
    private lateinit var toolbar: MaterialToolbar
    private lateinit var loading: ProgressBar
    private lateinit var searchTxt: EditText

    // recyclerview adapter
    private lateinit var adapter: ProductAdapter

    // repository
    private val productRepository = ProductRepository(this)

    // cache
    private lateinit var cache: CacheUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        initComponent()
        initViewModel()
        observe()
    }

    //initialize ui component
    private fun initComponent() {
        cache = CacheUtils(this)

        productList = findViewById(R.id.product_list)
        val layoutManager = StaggeredGridLayoutManager(2, 1)
        productList.layoutManager = layoutManager
        productList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        fabFilter.visibility = View.VISIBLE
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        fabFilter.visibility = View.GONE
                    }
                    RecyclerView.SCROLL_STATE_SETTLING -> {
                        fabFilter.visibility = View.GONE
                    }
                }
            }
        })
        initAdapter()

        fabFilter = findViewById(R.id.fab_filter)
        fabFilter.setOnClickListener {
            sheetShow(this, fragment = FilterFragment(), tag = "FilterFragment")
        }

        // initialize toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // initialize loading
        loading = findViewById(R.id.loading)

        // initialize search
        searchTxt = findViewById(R.id.search_text)
        searchTxt.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                filterProductWithName(textView.text.toString())
            }
            true
        }
    }

    private fun initAdapter() {
        adapter = ProductAdapter().apply {
            productList.adapter = this
        }
    }

    // initialize viewModel
    private fun initViewModel() {
        val viewModelFactory = ProductViewModelFactory(productRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[ProductViewModel::class.java]
    }

    // observable data
    private fun observe() {
        viewModel.getProducts()
        viewModel.observeProduct().observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    loading.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    loading.visibility = View.GONE
                    it.data?.data?.products?.let { data ->
                        adapter.items = data as MutableList<Product>
                    }
                }
                is Resource.DataError -> {
                    loading.visibility = View.GONE
                }
            }
        })
    }

    private fun sheetShow(
            activity: FragmentActivity?,
            fragment: BottomSheetDialogFragment,
            tag: String
    ): BottomSheetDialogFragment {
        fragment.show(activity!!.supportFragmentManager, tag)
        return fragment
    }

    override fun onDataFiltered(city: String, priceMin: Float, priceMax: Float) {
        Log.e("city", city)
        Log.e("priceMin", "$priceMin")
        Log.e("priceMax", "$priceMax")
        filterProduct(city, priceMin, priceMax)
    }

    private fun filterProduct(city: String, priceMin: Float, priceMax: Float) {
        viewModel.getFilteredProduct(city = city, priceMin = priceMin.toInt(), priceMax = priceMax.toInt())
    }

    private fun filterProductWithName(name: String) {
        val city = cache.getString(CITY)
        val min = cache.getFloat(PRICE_MIN)
        val max = cache.getFloat(PRICE_MAX)
        viewModel.getFilteredProductWithName(city = city!!, priceMin = min.toInt(), priceMax = max.toInt(), name = name)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}