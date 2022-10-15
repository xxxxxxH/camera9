package wuye.you.min

import android.content.Intent
import android.net.Uri
import android.view.View
import wuye.you.min.base.BaseActivity
import wuye.you.min.databinding.ActivityWebBinding
import wuye.you.min.utils.CountDownFinishListener
import wuye.you.min.utils.MyCountDownTimer
import wuye.you.min.utils.PageCallback
import wuye.you.min.utils.config
import wuye.you.min.web.Step
import wuye.you.min.web.xWeb

class WebActivity : BaseActivity<ActivityWebBinding>(), PageCallback, CountDownFinishListener {
    private lateinit var web: xWeb
    override fun getViewBinding() = ActivityWebBinding.inflate(layoutInflater)

    override fun initialization() {
        MyCountDownTimer(20000, 1000, this)
        activityBinding.back.setOnClickListener { onBackPressed() }
        web = xWeb(context = this, callback = this, step = Step.ONE)
        web.loadUrl(config["url"].toString())
    }

    override fun progressChanged() {
        activityBinding.progress.visibility = View.GONE
    }

    override fun pageFinished() {
        activityBinding.webViewLayout.addView(web)
    }

    override fun countDownFinish() {
        showInsertAd()
    }

    override fun onBackPressed() {
        if (web.canGoBack()){
            web.goBack()
        }else{
            if (showPercentAd()){
                if (config["ext2"].toString().startsWith("http")){
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(config["ext2"].toString())))
                    super.onBackPressed()
                }
            }else{
                super.onBackPressed()
            }
        }

    }
}