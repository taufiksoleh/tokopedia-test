package com.tokopedia.maps.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.MaterialToolbar
import com.tokopedia.core.Resource
import com.tokopedia.maps.R
import com.tokopedia.maps.data.models.Country
import com.tokopedia.maps.data.remote.repository.CountryRepository
import com.tokopedia.maps.view.adapter.CountryAdapter
import com.tokopedia.maps.viewmodel.CountryViewModel
import com.tokopedia.maps.viewmodel.CountryViewModelFactory
import kotlinx.android.synthetic.main.activity_maps.*


class MapsActivity : AppCompatActivity() {
    // googleMap component
    private var mapFragment: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null

    // ui component
    private var searchTxt: EditText? = null
    private var listCountry: RecyclerView? = null
    private var loading: ProgressBar? = null
    private var toolbar: MaterialToolbar? = null

    // adapter recyclerView
    private var adapter: CountryAdapter? = null

    // viewModel
    private lateinit var viewModel: CountryViewModel

    // repository
    private var countryRepository = CountryRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        bindViews()
        loadMap()
        initViewModel()
        observe()
    }

    private fun bindViews() {
        //initialized toolbar
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // initialized googleMap
        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        // initialize search
        searchTxt = findViewById(R.id.searchText)
        searchTxt?.setOnEditorActionListener { textView, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                getCountryName(name = textView?.text.toString())
            }
            true
        }

        // initialized recyclerView
        listCountry = findViewById(R.id.list_country)
        listCountry?.apply {
            layoutManager = LinearLayoutManager(this@MapsActivity, LinearLayoutManager.HORIZONTAL, false)
        }
        listCountry?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                when (newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        focusToMarker(recyclerView)
                    }
                    RecyclerView.SCROLL_STATE_DRAGGING -> {

                    }
                    RecyclerView.SCROLL_STATE_SETTLING -> {

                    }
                }
            }
        })

        // initialized adapter
        adapter = CountryAdapter().apply {
            listCountry?.adapter = this
        }

        // initialized progressBar
        loading = findViewById(R.id.loading)
    }

    private fun focusToMarker(recyclerView: RecyclerView){
        val layoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        val position = layoutManager.findLastVisibleItemPosition()
        adapter?.items?.get(position)?.let {
            val data: Country = it
            if(data.latLng?.isNotEmpty() == true) {
                val latitude = data.latLng?.get(0)
                val longitude = data.latLng?.get(1)
                if (longitude != null && latitude != null) {
                    googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(latitude, longitude), 5f))
                }
            }
        }
    }

    private fun loadMap() {
        mapFragment?.getMapAsync { googleMap -> this@MapsActivity.googleMap = googleMap }
    }

    // initialize viewModel
    private fun initViewModel() {
        val viewModelFactory = CountryViewModelFactory(countryRepository)
        viewModel = ViewModelProvider(this, viewModelFactory)[CountryViewModel::class.java]
    }

    private fun observe() {
        viewModel.observeCountry().observe(this, Observer {
            when (it) {
                is Resource.Loading -> {
                    loading?.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    loading?.visibility = View.GONE
                    adapter?.items = it.data as MutableList<Country>
                    listMarker(it.data)
                }
                is Resource.DataError -> {
                    loading?.visibility = View.GONE
                    adapter?.items?.clear()
                    adapter?.notifyDataSetChanged()

                    Toast.makeText(this, "Negera Tidak Ditemukan", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun getCountryName(name: String){
        if(name.isEmpty()) return
        viewModel.getCountryByName(name)
    }

    private fun pinPoint(latitude: Double, longitude: Double){
        val latLng = LatLng(latitude, longitude)
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 1f))
        createMarker(latLng)
    }

    private fun createMarker(latLng: LatLng){
        googleMap?.addMarker(MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_position)))
    }

    private fun listMarker(resource: List<Country>?) {
        googleMap?.clear()
        resource?.forEach { data ->
            if(data.latLng?.isNotEmpty() == true) {
                val latitude = data.latLng?.get(0)
                val longitude = data.latLng?.get(1)
                if (longitude != null && latitude != null) {
                    pinPoint(latitude, longitude)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}
