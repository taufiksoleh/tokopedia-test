package com.tokopedia.maps.data.remote.service

import com.tokopedia.maps.data.models.Country
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface CountryApiInterface {
    @GET("/{path}/{keyword}")
    fun search(
            @Header("x-rapidapi-key") apiKey: String,
            @Path("path") path: String,
            @Path("keyword") keyword: String
    ): Single<List<Country>>
}