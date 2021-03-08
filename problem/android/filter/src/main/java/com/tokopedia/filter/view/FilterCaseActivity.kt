package com.tokopedia.filter.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.tokopedia.core.loadFile
import com.tokopedia.filter.R

class FilterCaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_problem_simulation)
        val webView = findViewById<WebView>(R.id.webView)
        webView.loadFile("filter.html")
        findViewById<View>(R.id.btn_simulate).setOnClickListener {
            startActivity(Intent(this, ProductActivity::class.java))
        }
    }

}