package wuye.you.min.web

import android.content.Context
import android.webkit.WebChromeClient
import android.webkit.WebView
import wuye.you.min.utils.print

class xWebChromeClient @JvmOverloads constructor(
    context: Context,
    private val listener: xWebListener
) : WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        if (newProgress == 100) {
            "newProgress = 100".print()
            listener.progressChanged(newProgress)
        }
    }
}