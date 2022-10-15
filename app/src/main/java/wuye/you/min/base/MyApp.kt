package wuye.you.min.base

import android.app.Application
import android.os.Build
import android.webkit.WebView
import com.anythink.core.api.ATSDK
import com.anythink.core.api.NetTrafficeCallback
import com.applovin.sdk.AppLovinMediationProvider
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkSettings
import com.tencent.mmkv.MMKV
import wuye.you.min.BuildConfig
import wuye.you.min.utils.LOVIN_APP_KEY
import wuye.you.min.utils.TOP_ON_APP_ID
import wuye.you.min.utils.TOP_ON_APP_KEY

class MyApp : Application() {
    companion object {
        lateinit var instance: MyApp
        lateinit var lovinSdk: AppLovinSdk
    }

    override fun onCreate() {
        super.onCreate()
        MMKV.initialize(this)
        instance = this
        lovinSdk = AppLovinSdk.getInstance(
            LOVIN_APP_KEY.reversed(),
            AppLovinSdkSettings(this),
            this
        )
        lovinSdk.apply {
            mediationProvider = AppLovinMediationProvider.MAX
            initializeSdk()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = getProcessName()
            if (packageName != processName) {
                WebView.setDataDirectorySuffix(processName)
            }
        }
        ATSDK.checkIsEuTraffic(this, object : NetTrafficeCallback {
            override fun onResultCallback(isEU: Boolean) {
                if (isEU && ATSDK.getGDPRDataLevel(this@MyApp) == ATSDK.UNKNOWN) {
                    ATSDK.showGdprAuth(this@MyApp)
                }
            }

            override fun onErrorCallback(errorMsg: String) {
            }
        })

        ATSDK.setNetworkLogDebug(BuildConfig.DEBUG)
        ATSDK.integrationChecking(this@MyApp)
        ATSDK.init(
            this@MyApp,
            TOP_ON_APP_ID,
            TOP_ON_APP_KEY
        )
    }
}