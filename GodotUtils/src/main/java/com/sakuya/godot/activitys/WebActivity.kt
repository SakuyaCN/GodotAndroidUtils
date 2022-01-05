package com.sakuya.godot.activitys

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.sakuya.godot.R

class WebActivity : AppCompatActivity() {

    private lateinit var webView:WebView
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        findViewById<ImageButton>(R.id.close).setOnClickListener { finish() }
        webView = findViewById(R.id.webView)
        toolbar = findViewById(R.id.toolbar)
        initWebViewSettings()
        var url = intent.getStringExtra("url")?:""
        webView.loadUrl(url)
    }

    private fun initWebViewSettings() {
        val webSetting = webView.settings
        webSetting.allowFileAccess = true
        webSetting.allowContentAccess = true
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls = true
        webSetting.useWideViewPort = true
        webSetting.loadWithOverviewMode = true
        webSetting.setSupportMultipleWindows(false)
        webSetting.setAppCacheEnabled(true)
        webSetting.setDomStorageEnabled(true)
        webSetting.setJavaScriptEnabled(true)
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE)
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        webSetting.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.clearCache(true)

        webView.webChromeClient = object : WebChromeClient() {

            override fun onReceivedTitle(p0: WebView?, p1: String?) {
                super.onReceivedTitle(p0, p1)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    toolbar.title = p1!!
                }
            }
        }
    }
}