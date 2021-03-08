package com.tokopedia.maps.data.models

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName

data class Country(
        @SerializedName("id")
        var id: Int,
        @SerializedName("name")
        var countryName: String?,
        @SerializedName("capital")
        var capitalCity: String?,
        @SerializedName("callingCodes")
        var callingCode: List<String?>?,
        @SerializedName("population")
        var population: Long?,
        @SerializedName("latlng")
        var latLng: List<Double?>?
)
