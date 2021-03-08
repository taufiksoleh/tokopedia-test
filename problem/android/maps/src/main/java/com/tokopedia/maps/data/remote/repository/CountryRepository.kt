package com.tokopedia.maps.data.remote.repository

import com.tokopedia.core.Constants
import com.tokopedia.maps.data.models.Country
import com.tokopedia.maps.data.remote.service.CountryApiService
import io.reactivex.Single

class CountryRepository: ICountryRepository {
    override fun getGetCountryByName(name: String): Single<List<Country>> {
        return CountryApiService.getClient.search(
                apiKey = Constants.RAPID_API_KEY,
                path = "name",
                keyword = name
        )
    }
}