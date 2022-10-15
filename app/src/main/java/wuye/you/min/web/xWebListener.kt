package wuye.you.min.web

import android.webkit.WebView

interface xWebListener {
    fun progressChanged(newProgress: Int)
    fun pageFinished(cookieString: String, view: WebView?, url: String?)
}