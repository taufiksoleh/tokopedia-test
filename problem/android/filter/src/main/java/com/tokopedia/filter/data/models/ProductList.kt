package com.tokopedia.filter.data.models

import com.google.gson.annotations.SerializedName

data class ProductList (
        @SerializedName("products")
        val products: List<Product>?
)