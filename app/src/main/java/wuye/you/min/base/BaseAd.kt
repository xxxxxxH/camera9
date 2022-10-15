package wuye.you.min.base

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.anythink.splashad.api.ATSplashAd
import com.applovin.mediation.ads.MaxAdView
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView

abstract class BaseAd {
    protected abstract fun getOpenAd(): ATSplashAd
    protected abstract fun getBannerAd(): MaxAdView
    protected abstract fun getInsertAd(): MaxInterstitialAd
    protected abstract fun getNativeAd():MaxNativeAdLoader
}