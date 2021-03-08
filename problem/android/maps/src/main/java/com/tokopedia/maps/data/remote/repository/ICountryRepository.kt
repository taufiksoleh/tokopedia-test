package com.tokopedia.maps.data.remote.repository

import com.tokopedia.maps.data.models.Country
import io.reactivex.Single

interface ICountryRepository {
    fun getGetCountryByName(name: String): Single<List<Country>>
}