package com.tokopedia.filter.data.models

import com.google.gson.annotations.SerializedName

data class ProductResource(
        @SerializedName("data")
        val data: ProductList?
)
