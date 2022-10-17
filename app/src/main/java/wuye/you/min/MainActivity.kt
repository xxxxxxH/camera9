package wuye.you.min

import android.Manifest
import android.content.Intent
import android.view.View
import com.hjq.permissions.XXPermissions
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import wuye.you.min.base.BaseActivity
import wuye.you.min.databinding.ActivityMainBinding
import wuye.you.min.event.xEvent
import wuye.you.min.utils.config
import wuye.you.min.utils.getConfig
import wuye.you.min.utils.getId
import wuye.you.min.utils.isLogin

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
    private var isShowAd = false
    override fun initialization() {
        EventBus.getDefault().register(this)
        XXPermissions.with(this)
            .permission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .request { _, all ->
                if (all) {
                    getId {
                        getConfig(it) {
                            if (isLogin) {
                                showAd()
                            } else {
                                if (config["l"] == "1") {
                                    activityBinding.loginBtn.visibility = View.VISIBLE
                                } else {
                                    showAd()
                                }
                            }
                        }
                    }
                    activityBinding.loginBtn.setOnClickListener {
                        startActivity(Intent(this, WebActivity::class.java))
                    }
                } else {
                    finish()
                }
            }

    }

    private fun showAd() {
        if (showOddsAd(activityBinding.rootLayout)) {
            isShowAd = true
            return
        }
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onEvent(e: xEvent) {
        val msg = e.getMessage()
        when {
            msg[0].toString() == "submit success" -> {
                finish()
            }
            msg[0].toString() == "open dismiss" -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
            msg[0].toString() == "insert hidden" -> {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}