package com.tokopedia.testproject

import com.tokopedia.filter.view.ProductActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FilterViewInstrumentationTest {

    @Before
    fun setup(){
        Robolectric.buildActivity(ProductActivity::class.java)
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

    }

    @Test
    fun testDataFilterShow(){

    }

    @Test
    fun testMultipleSelectFilter(){

    }
}