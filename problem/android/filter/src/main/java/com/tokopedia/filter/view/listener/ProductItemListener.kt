package com.tokopedia.filter.view.listener

import com.tokopedia.filter.data.models.Product

interface ProductItemListener {
    fun onItemSelected(product: Product)
}