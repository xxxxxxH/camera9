package wuye.you.min

import com.applovin.mediation.MaxAd
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdView
import wuye.you.min.base.BaseActivity
import wuye.you.min.databinding.ActivityHomeBinding
import wuye.you.min.utils.AdImpl
import wuye.you.min.utils.HomeItemClickListener
import wuye.you.min.utils.PageCallback
import wuye.you.min.utils.print
import wuye.you.min.web.xWeb
import wuye.you.min.widget.HomeItem

class HomeActivity : BaseActivity<ActivityHomeBinding>(), PageCallback, HomeItemClickListener {
    private lateinit var web: xWeb
    override fun getViewBinding() = ActivityHomeBinding.inflate(layoutInflater)

    override fun initialization() {
       val ad = AdImpl(this).getNativeImpl()
        ad.setNativeAdListener(object :MaxNativeAdListener(){
            override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd?) {
                super.onNativeAdLoaded(p0, p1)
                p0?.let {
                    activityBinding.adView.removeAllViews()
                    activityBinding.adView.addView(it)
                }
            }
        })
        activityBinding.iconLayout.addView(HomeItem(this, imgRes = R.mipmap.sticker_icon, title = "Sticker", listener = this))
        activityBinding.iconLayout.addView(HomeItem(this, imgRes = R.mipmap.slimming_icon, title = "Slimming", listener = this))
        activityBinding.iconLayout.addView(HomeItem(this, imgRes = R.mipmap.cartoon_icon, title = "Cartoon", listener = this))
        activityBinding.iconLayout.addView(HomeItem(this, imgRes = R.mipmap.a_icon, title = "Age Alter", listener = this))
//        web = xWeb(context = this, callback = this, step = Step.TWO)
//        web.loadUrl(URL_ADS)
    }

    override fun progressChanged() {

    }

    override fun pageFinished() {

    }

    override fun itemClick(title: String) {
        if (showPercentAd()){
            "show ad".print()
        }else{
            "other action".print()
        }

    }
}