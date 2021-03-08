package com.tokopedia.filter.view

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.filter.R
import com.tokopedia.filter.data.local.ProductRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ProductActivityTest {
    private lateinit var activity: ProductActivity
    private lateinit var productRepository: ProductRepository

    @Before
    fun setup(){
        activity = Robolectric.buildActivity(ProductActivity::class.java)
                .get()

        productRepository = ProductRepository(activity.applicationContext)
    }

    @Test
    @Throws(Exception::class)
    fun shouldNotBeNull() {
        assertNotNull(activity)
    }


    @Test
    fun testErrorLoading(){

    }

    @Test
    fun testEmptyResult(){

    }

    @Test
    fun testLoading(){

    }

    @Test
    fun testPagination(){

    }

    @Test
    fun testDataShow(){
        val product = productRepository.getProduct()
        val productCount = product?.data?.products?.size
        val recyclerView = activity.findViewById<RecyclerView>(R.id.product_list)
        var textView: TextView? = null
        if (productCount != null) {
            recyclerView.scrollToPosition(productCount - 1)
            textView = recyclerView.findViewHolderForLayoutPosition(productCount)?.itemView?.findViewById(R.id.name)
        }

        val expectedTextView = "Bervin Air Protection BAP-11 Penahan Angin AC Talang AC - Putih"
        assertEquals(expectedTextView, textView?.text.toString())
    }

    @Test
    fun testDataFilterShow(){

    }

    @Test
    fun testMultipleSelectFilter(){

    }
}