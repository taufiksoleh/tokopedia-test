package com.tokopedia.minimumpathsum

object Solution {

    fun minimumPathSum(matrix: Array<IntArray>): Int {
        // TODO, find a path from top left to bottom right which minimizes the sum of all numbers along its path, and return the sum
        // below is stub
        val m: Int = matrix.size
        val n: Int = matrix[0].size
        val dp = Array(m) { IntArray(n) }

        // define finish point
        dp[m - 1][n - 1] = matrix[m - 1][n - 1]

        // sum vertically from finish point up
        for (i in n - 2 downTo 0) {
            dp[m - 1][i] = matrix[m - 1][i] + dp[m - 1][i + 1]
        }

        // sum horizontally from finish point aside
        for (i in m - 2 downTo 0) {
            dp[i][n - 1] = matrix[i][n - 1] + dp[i + 1][n - 1]
        }

        // path finding
        for (j in n - 2 downTo 0) {
            for (i in m - 2 downTo 0) {
                dp[i][j] = dp[i + 1][j].coerceAtMost((dp[i][j + 1])) + matrix[i][j]
            }
        }

        return dp[0][0]
    }

}
