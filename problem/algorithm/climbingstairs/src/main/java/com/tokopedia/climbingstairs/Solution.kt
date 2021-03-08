package com.tokopedia.climbingstairs

object Solution {
    fun climbStairs(n: Int): Long? {
        // TODO, return in how many distinct ways can you climb to the top. Each time you can either climb 1 or 2 steps.
        return calculate(n)
    }

    private fun calculate(n: Int): Long? {
        // requirement limit (1 <= n < 90)
        if (n in 1..89){
            // init
            val dp = Array(n + 1){0}

            // looping and calculate step
            dp.forEachIndexed() { index, _ ->
                when (index) {
                    0 -> dp[index] = 0
                    1 -> dp[index] = 1
                    2 -> dp[index] = 2
                    else -> dp[index] = dp[index - 1] + dp[index - 2]
                }
            }
            return dp[n].toLong()
        }

        return null
    }
}
