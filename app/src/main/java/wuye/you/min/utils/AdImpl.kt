package wuye.you.min.utils

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.anythink.core.api.ATAdInfo
import com.anythink.core.api.AdError
import com.anythink.splashad.api.ATSplashAd
import com.anythink.splashad.api.ATSplashAdListener
import com.anythink.splashad.api.IATSplashEyeAd
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import wuye.you.min.base.BaseAd
import wuye.you.min.base.MyApp

@SuppressLint("StaticFieldLeak")
class AdImpl(private var activity: AppCompatActivity) : BaseAd(), ATSplashAdListener,
    MaxAdListener {
    companion object {
        lateinit var splashAd: ATSplashAd
        lateinit var insertAd: MaxInterstitialAd
        lateinit var bannerAd: MaxAdView
        lateinit var nativeAd: MaxNativeAdLoader
    }

    override fun getOpenAd(): ATSplashAd {
        splashAd = ATSplashAd(MyApp.instance, TOP_ON_OPEN_AD_ID, this)
        splashAd.loadAd()
        "open $splashAd".print()
        return splashAd
    }

    fun getOpenAdImpl() = getOpenAd()


    override fun getInsertAd(): MaxInterstitialAd {
        insertAd = MaxInterstitialAd(LOVIN_INSERT_AD_ID, MyApp.lovinSdk, activity)
        insertAd.setListener(this)
        insertAd.loadAd()
        "insert $insertAd".print()
        return insertAd
    }

    fun getInsertImpl() = getInsertAd()

    override fun getBannerAd(): MaxAdView {
        bannerAd = MaxAdView(LOVIN_BANNER_AD_ID, MyApp.lovinSdk, MyApp.instance)
        bannerAd.loadAd()
        "banner $bannerAd".print()
        return bannerAd
    }

    fun getBannerAdImpl() = getBannerAd()

    override fun getNativeAd(): MaxNativeAdLoader {
        nativeAd = MaxNativeAdLoader(LOVIN_NATIVE_AD_ID, MyApp.lovinSdk, MyApp.instance)
        nativeAd.loadAd()
        "native $nativeAd".print()
        return nativeAd
    }

    fun getNativeImpl() = getNativeAd()

    override fun onAdLoaded() {
        "open load".print()
    }

    override fun onNoAdError(p0: AdError?) {
        "open error:$p0".print()
        activity.lifecycleScope.launch(Dispatchers.IO) {
            delay(3500)
            splashAd.onDestory()
            getOpenAd()
            splashAd.loadAd()
        }
    }

    override fun onAdShow(p0: ATAdInfo?) {
        "open show".print()
    }

    override fun onAdClick(p0: ATAdInfo?) {
        "open click".print()
    }

    override fun onAdDismiss(p0: ATAdInfo?, p1: IATSplashEyeAd?) {
        "open dismiss".print()
        activity.lifecycleScope.launch(Dispatchers.IO) {
            delay(3500)
            splashAd.onDestory()
            getOpenAd()
            splashAd.loadAd()
        }
    }

    override fun onAdLoaded(ad: MaxAd?) {
        "insert load".print()
    }

    override fun onAdDisplayed(ad: MaxAd?) {
        "insert displayed".print()
    }

    override fun onAdHidden(ad: MaxAd?) {
        "insert hidden".print()
        activity.lifecycleScope.launch(Dispatchers.IO) {
            insertAd.destroy()
            delay(3500)
            getInsertAd()
            insertAd.loadAd()
        }
    }

    override fun onAdClicked(ad: MaxAd?) {
        "insert clicked".print()
    }

    override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
        "insert loadFailed:$error".print()
        activity.lifecycleScope.launch(Dispatchers.IO) {
            insertAd.destroy()
            delay(3500)
            getInsertAd()
            insertAd.loadAd()
        }
    }

    override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
        "insert displayFailed:$error".print()
        activity.lifecycleScope.launch(Dispatchers.IO) {
            insertAd.destroy()
            delay(3500)
            getInsertAd()
            insertAd.loadAd()
        }
    }
}