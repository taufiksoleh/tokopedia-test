package com.tokopedia.minimumpathsum

import org.junit.Assert.*
import org.junit.Test

class SolutionTest {
    @Test
    fun minimumPathSum() {
        val matrix = arrayOf(
                intArrayOf(1, 2, 1, 9),
                intArrayOf(1, 5, 1, 1),
                intArrayOf(4, 2, 1, 1)
        )
        val value = Solution.minimumPathSum(matrix)
        val expectedValue = 7
        assertEquals(expectedValue, value)
    }
}