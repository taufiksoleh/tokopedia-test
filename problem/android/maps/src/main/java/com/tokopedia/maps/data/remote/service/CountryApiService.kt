package com.tokopedia.maps.data.remote.service

import com.tokopedia.core.Constants
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object CountryApiService {
    private val TAG = "--ApiService"

    var getClient = Retrofit.Builder()
            .baseUrl(Constants.API_BASE_PATH)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(ApiWorker.gsonConverter)
            .client(ApiWorker.client)
            .build()
            .create(CountryApiInterface::class.java)
}