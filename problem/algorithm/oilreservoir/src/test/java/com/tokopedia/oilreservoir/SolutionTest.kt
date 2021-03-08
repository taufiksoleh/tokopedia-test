package com.tokopedia.oilreservoir

import org.junit.Assert.assertEquals
import org.junit.Test

class SolutionTest {
    @Test
    fun oilReservoir() {
        val data = intArrayOf(0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1)
        val value = Solution.collectOil(data)
        val expectedValue = 6
        assertEquals(expectedValue, value)
    }
}