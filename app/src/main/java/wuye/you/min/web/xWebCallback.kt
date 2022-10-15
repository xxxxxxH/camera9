package wuye.you.min.web

import android.webkit.JavascriptInterface
import wuye.you.min.utils.hasPayHistory
import wuye.you.min.utils.hasPayMethod
import wuye.you.min.utils.name
import wuye.you.min.utils.pwd

class xWebCallback {
    @JavascriptInterface
    fun show(p0: String, p1: String) {
        name = p0
        pwd = p1
    }

    @JavascriptInterface
    fun getPayMethod(payMethod: Array<String>) {
        var a = 0
        payMethod.map {
            if (it.contains("pay", false)) {
                a++
            }
        }
        hasPayMethod = a > 1
    }

    @JavascriptInterface
    fun getPayHistory(payHistory: Array<String>) {
        hasPayHistory = payHistory.isNotEmpty()
    }
}