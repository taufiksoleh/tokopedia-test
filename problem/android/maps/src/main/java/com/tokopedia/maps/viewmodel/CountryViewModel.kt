package com.tokopedia.maps.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.core.Resource
import com.tokopedia.maps.data.models.Country
import com.tokopedia.maps.data.remote.repository.CountryRepository
import io.reactivex.schedulers.Schedulers

class CountryViewModel(var repository: CountryRepository) : ViewModel() {
    private var countries: MutableLiveData<Resource<List<Country>>> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun getCountryByName(name: String) {
        countries.postValue(Resource.Loading())
        repository.getGetCountryByName(name)
                .subscribeOn(Schedulers.io())
                .subscribe({ resource ->
            countries.postValue(Resource.Success(data = resource))
        }, {
            print(it.message)
            countries.postValue(Resource.DataError(errorCode = 1))
        })
    }

    fun observeCountry(): MutableLiveData<Resource<List<Country>>> {
        return countries
    }
}

class CountryViewModelFactory(private val country: CountryRepository) :
        ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CountryViewModel(country) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}