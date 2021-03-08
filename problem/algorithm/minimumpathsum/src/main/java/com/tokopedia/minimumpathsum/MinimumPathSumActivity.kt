package com.tokopedia.minimumpathsum

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.core.loadFile

class MinimumPathSumActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_problem)
        val webView = findViewById<WebView>(R.id.webView)
        webView.loadFile("minimum_path_sum.html");

        // example of how to call the function
        val number = Solution.minimumPathSum(arrayOf(
                intArrayOf(1, 2, 1, 9),
                intArrayOf(1, 5, 1, 1),
                intArrayOf(4, 2, 1, 1)))

        println(number)
    }

}