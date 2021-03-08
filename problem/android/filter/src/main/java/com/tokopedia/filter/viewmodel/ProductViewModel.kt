package com.tokopedia.filter.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.core.Resource
import com.tokopedia.filter.data.local.ProductRepository
import com.tokopedia.filter.data.models.City
import com.tokopedia.filter.data.models.Price
import com.tokopedia.filter.data.models.ProductResource
import io.reactivex.Observable

class ProductViewModel(private val local: ProductRepository) : ViewModel() {
    private val products: MutableLiveData<Resource<ProductResource>> = MutableLiveData()
    private val city: MutableLiveData<Resource<List<City>>> = MutableLiveData()
    private val price: MutableLiveData<Resource<Price>> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun getProducts() {
        Observable.create<ProductResource> {
            local.getProduct()?.let { product -> it.onNext(product) }
            it.onComplete()
        }.subscribe({
            products.postValue(
                    Resource.Success(data = it)
            )
        }, {
            products.postValue(
                    Resource.DataError(errorCode = 1)
            )
        })
    }

    @SuppressLint("CheckResult")
    fun getFilteredProduct(city: String, priceMin: Int, priceMax: Int) {
        Observable.create<ProductResource> {
            local.getFilteredProduct(city, priceMin, priceMax).let { product -> it.onNext(product) }
            it.onComplete()
        }.subscribe({
            products.postValue(
                    Resource.Success(data = it)
            )
        }, {
            products.postValue(
                    Resource.DataError(errorCode = 1)
            )
        })
    }

    @SuppressLint("CheckResult")
    fun getFilteredProductWithName(city: String, priceMin: Int, priceMax: Int, name: String) {
        Observable.create<ProductResource> {
            local.getFilteredProductWithName(city, priceMin, priceMax, name).let { product -> it.onNext(product) }
            it.onComplete()
        }.subscribe({
            products.postValue(
                    Resource.Success(data = it)
            )
        }, {
            products.postValue(
                    Resource.DataError(errorCode = 1)
            )
        })
    }

    @SuppressLint("CheckResult")
    fun getMostCity(limit: Int){
        Observable.create<List<City>> {
            local.getMostCity(limit).let { city -> it.onNext(city) }
            it.onComplete()
        }.subscribe({
            city.postValue(
                    Resource.Success(data = it)
            )
        }, {
            city.postValue(
                    Resource.DataError(errorCode = 1)
            )
        })
    }

    @SuppressLint("CheckResult")
    fun getPrice(){
        Observable.create<Price> {
            local.getPrice().let { price -> it.onNext(price) }
            it.onComplete()
        }.subscribe({
            price.postValue(
                    Resource.Success(data = it)
            )
        }, {
            city.postValue(
                    Resource.DataError(errorCode = 1)
            )
        })
    }

    fun observeProduct(): LiveData<Resource<ProductResource>> {
        return products
    }

    fun observeCity(): LiveData<Resource<List<City>>> {
        return city
    }

    fun observePrice() : LiveData<Resource<Price>>{
        return price
    }
}

class ProductViewModelFactory(private val product: ProductRepository) :
        ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(product) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}