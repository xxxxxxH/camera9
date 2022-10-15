package wuye.you.min.web

import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import wuye.you.min.utils.print

class xWebViewClient @JvmOverloads constructor(private val listener: xWebListener) :
    WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        view?.loadUrl(request?.url.toString())
        return true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        "onPageFinished".print()
        val cookieManager = CookieManager.getInstance()
        val cookieStr = cookieManager.getCookie(url)
        listener.pageFinished(cookieStr, view, url)
    }
}