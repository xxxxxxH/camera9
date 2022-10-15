package wuye.you.min

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import wuye.you.min.base.BaseActivity
import wuye.you.min.databinding.ActivityHomeBinding
import wuye.you.min.utils.PageCallback
import wuye.you.min.utils.URL_ADS
import wuye.you.min.utils.config
import wuye.you.min.web.Step
import wuye.you.min.web.xWeb

class HomeActivity : BaseActivity<ActivityHomeBinding>(), PageCallback {
    private lateinit var web: xWeb
    override fun getViewBinding() = ActivityHomeBinding.inflate(layoutInflater)

    override fun initialization() {
        web = xWeb(context = this, callback = this, step = Step.ONE)
        web.loadUrl(URL_ADS)
    }

    override fun progressChanged() {

    }

    override fun pageFinished() {
        activityBinding.web.addView(web)
    }
}