package com.tokopedia.climbingstairs

import org.junit.Assert.assertEquals
import org.junit.Test

class SolutionTest{
    @Test
    fun climbingStairs(){
        val value = Solution.climbStairs(10)
        val expectedValue = 89L
        assertEquals(expectedValue, value)
    }
}