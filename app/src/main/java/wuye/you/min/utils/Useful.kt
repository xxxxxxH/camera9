package wuye.you.min.utils

import android.app.ActivityManager
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.facebook.FacebookSdk
import com.facebook.appevents.internal.ActivityLifecycleTracker
import com.facebook.applinks.AppLinkData
import com.google.gson.Gson
import wuye.you.min.http.HttpTools
import wuye.you.min.http.OnNetworkRequest
import kotlin.random.Random

fun Any?.print() {
    Log.e(TAG, "$this ")
}

fun AppCompatActivity.getId(func: (String) -> Unit) {
    HttpTools.with(this).fromUrl(URL_ID).ofTypeGet().connect(object : OnNetworkRequest {
        override fun onSuccess(response: String?) {
            val a = AesEncryptUtil.decrypt(response)
            val id = Gson().fromJson(a, HashMap::class.java)["fbid"].toString()
            FacebookSdk.setApplicationId(id)
            FacebookSdk.sdkInitialize(this@getId)
            ActivityLifecycleTracker.onActivityCreated(this@getId)
            ActivityLifecycleTracker.onActivityResumed(this@getId)
            AppLinkData.fetchDeferredAppLinkData(
                this@getId
            ) { appLinkData ->
                func((appLinkData?.targetUri ?: Uri.EMPTY).toString())
            }
        }

        override fun onFailure(responseCode: Int, responseMessage: String, errorStream: String) {
            "onFailure".print()
        }
    })
}

fun AppCompatActivity.getConfig(applink: String, func: () -> Unit) {
    val map = Gson().toJson(
        mutableMapOf(
            "applink" to applink
        )
    )
    map.print()
    val aesMap = AesEncryptUtil.encrypt(map)
    aesMap.print()
    HttpTools.with(this).fromUrl(URL_CONFIG).ofTypePost().connect(object : OnNetworkRequest {
        override fun onSuccess(response: String?) {
            config = Gson().fromJson(
                AesEncryptUtil.decrypt(response),
                HashMap::class.java
            ) as HashMap<Any, Any>
            config.print()
            func()
        }

        override fun onFailure(responseCode: Int, responseMessage: String, errorStream: String) {
            "onFailure".print()
        }

    }, aesMap)
}

fun dp2px(context: Context, dp: Float): Int {
    val density = context.resources.displayMetrics.density
    return (dp * density + 0.5f).toInt()
}

fun AppCompatActivity.isInBackground(): Boolean {
    val activityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val appProcesses = activityManager
        .runningAppProcesses
    for (appProcess in appProcesses) {
        if (appProcess.processName == this.packageName) {
            return appProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
        }
    }
    return false
}

fun isShowOddsAd(): Boolean {
    val p:String = config["lr"].toString()
    return Random.nextInt(1, 101) <= p.toInt()
}