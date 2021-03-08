package com.tokopedia.filter.view.listener

import com.tokopedia.filter.data.models.City

interface CityItemListener {
    fun onItemSelected(city: City)
}