package com.tokopedia.filter.view

interface OnDataFiltered {
        fun onDataFiltered(city: String, priceMin: Float, priceMax: Float)
    }