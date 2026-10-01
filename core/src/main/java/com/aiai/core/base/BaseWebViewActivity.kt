/*
 * Copyright (c) 2024 爱Ai (AiAi) Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aiai.core.base

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.viewbinding.ViewBinding

/**
 * WebView 基类。
 *
 * 功能：
 * - JS 交互
 * - 进度条
 * - 错误处理
 * - 缓存管理
 * - 下载处理
 */
abstract class BaseWebViewActivity<VB : ViewBinding> : BaseActivity<VB>() {

    protected lateinit var webView: WebView
    protected var progressBar: ProgressBar? = null

    /** WebView 加载的 URL。 */
    protected open val url: String = ""

    /** JS 接口名称。 */
    protected open val jsInterfaceName: String = "Android"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWebView()
        loadUrl(url)
    }

    /** 初始化 WebView。 */
    @SuppressLint("SetJavaScriptEnabled")
    protected open fun initWebView() {
        webView = WebView(this)

        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            setAppCacheEnabled(true)
            cacheMode = WebSettings.LOAD_DEFAULT
            useWideViewPort = true
            loadWithOverviewMode = true
            setSupportZoom(true)
            builtInZoomControls = true
            displayZoomControls = false
            allowFileAccess = true
            allowContentAccess = true
            mediaPlaybackRequiresUserGesture = false
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webView.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar?.visibility = View.VISIBLE
                onPageStart(url)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar?.visibility = View.GONE
                onPageFinish(url)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                onPageError(error?.description?.toString())
            }

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url?.toString() ?: return false
                return shouldOverrideUrl(url)
            }
        }

        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar?.progress = newProgress
                onProgressChanged(newProgress)
            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                onReceivedTitle(title)
            }
        }
    }

    /**
     * 加载 URL。
     *
     * @param url 要加载的地址
     */
    open fun loadUrl(url: String) {
        if (url.isNotEmpty()) {
            webView.loadUrl(url)
        }
    }

    /**
     * 加载 HTML 内容。
     *
     * @param data HTML 内容
     * @param mimeType MIME 类型
     * @param encoding 编码
     */
    open fun loadData(data: String, mimeType: String = "text/html", encoding: String = "utf-8") {
        webView.loadDataWithBaseURL(null, data, mimeType, encoding, null)
    }

    /**
     * 添加 JS 接口。
     *
     * @param interfaceObj 接口对象
     * @param name 接口名称
     */
    protected open fun addJavascriptInterface(interfaceObj: Any, name: String = jsInterfaceName) {
        webView.addJavascriptInterface(interfaceObj, name)
    }

    /**
     * 页面开始加载回调。
     */
    protected open fun onPageStart(url: String?) {}

    /**
     * 页面加载完成回调。
     */
    protected open fun onPageFinish(url: String?) {}

    /**
     * 页面加载错误回调。
     */
    protected open fun onPageError(error: String?) {}

    /**
     * 进度变化回调。
     */
    protected open fun onProgressChanged(progress: Int) {}

    /**
     * 收到标题回调。
     */
    protected open fun onReceivedTitle(title: String?) {}

    /**
     * 拦截 URL 加载。
     *
     * @return true 表示已处理
     */
    protected open fun shouldOverrideUrl(url: String): Boolean = false

    /**
     * 是否可以后退。
     */
    open fun canGoBack(): Boolean = webView.canGoBack()

    /** 后退。 */
    open fun goBack() {
        if (webView.canGoBack()) {
            webView.goBack()
        }
    }

    /** 前进。 */
    open fun goForward() {
        if (webView.canGoForward()) {
            webView.goForward()
        }
    }

    /** 刷新。 */
    open fun reload() {
        webView.reload()
    }

    /** 停止加载。 */
    open fun stopLoading() {
        webView.stopLoading()
    }

    override fun onDestroy() {
        webView.apply {
            stopLoading()
            removeAllViews()
            destroy()
        }
        super.onDestroy()
    }
}
