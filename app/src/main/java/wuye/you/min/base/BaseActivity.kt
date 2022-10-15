package wuye.you.min.base

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.anythink.splashad.api.ATSplashAd
import com.applovin.mediation.ads.MaxInterstitialAd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wuye.you.min.R
import wuye.you.min.utils.*

abstract class BaseActivity<T : ViewBinding> : AppCompatActivity() {
    lateinit var activityBinding: T
    private val binding get() = activityBinding
    private var isBackground = false
    private lateinit var openAd: ATSplashAd
    private lateinit var insertAd: MaxInterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBinding = getViewBinding()
        setContentView(binding.root)
        openAd = AdImpl(this).getOpenAdImpl()
        insertAd = AdImpl(this).getInsertImpl()
        initialization()
        addBannerAd()
    }

    protected abstract fun getViewBinding(): T

    abstract fun initialization()

    private fun addBannerAd() {
        val content = findViewById<ViewGroup>(android.R.id.content)
        val frameLayout = FrameLayout(this)
        val p = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        frameLayout.layoutParams = p

        val linearLayout = LinearLayout(this)
        val p1 = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        linearLayout.layoutParams = p1
        val banner = AdImpl(this@BaseActivity).getBannerAdImpl()
        lifecycleScope.launch(Dispatchers.IO) {
            delay(3000)
            withContext(Dispatchers.Main) {
                val p2 =
                    LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        dp2px(this@BaseActivity, 50f)
                    )
                p2.gravity = Gravity.BOTTOM
                banner.layoutParams = p2
                linearLayout.addView(banner)
                frameLayout.addView(linearLayout)
                content.addView(frameLayout)
            }
        }
    }

    fun showOpenAd(v: ViewGroup): Boolean {
        if (openAd.isAdReady) {
            openAd.show(this, v)
            return true
        }
        return false
    }

    fun showInsertAd(): Boolean {
        if (insertAd.isReady) {
            insertAd.showAd()
            return true
        }
        insertAd = AdImpl(this).getInsertImpl()
        return false
    }

    fun showOddsAd(v: ViewGroup): Boolean {
        return if (config["i"] == "1") {
            showPercentAd()
        } else {
            showOpenAd(v)
        }
    }

    fun showPercentAd():Boolean{
        if (isShowOddsAd()){
            return showInsertAd()
        }
        return false
    }

    override fun onStop() {
        super.onStop()
        isBackground = isInBackground()
    }

    override fun onResume() {
        super.onResume()
        if (isBackground) {
            "onresuem".print()
            isBackground = false
            val content = findViewById<ViewGroup>(android.R.id.content)
            (content.getTag(R.id.open_ad_view_id) as? FrameLayout)?.let {
                showOddsAd(it)
            } ?: kotlin.run {
                FrameLayout(this).apply {
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    content.addView(this)
                    content.setTag(R.id.open_ad_view_id, this)
                    showOddsAd(this)
                }
            }
        }
    }
}