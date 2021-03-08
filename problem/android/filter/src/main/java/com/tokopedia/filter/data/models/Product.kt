package com.tokopedia.filter.data.models

import com.google.gson.annotations.SerializedName

data class Product(
        @SerializedName("id")
        val id: Int?,
        @SerializedName("name")
        val name: String?,
        @SerializedName("imageUrl")
        val imageUrl: String?,
        @SerializedName("priceInt")
        val priceInt: Int?,
        @SerializedName("discountPercentage")
        val discountPercentage: Int?,
        @SerializedName("slashedPriceInt")
        val slashedPriceInt: Int?,
        @SerializedName("shop")
        val shop: Shop?
)
