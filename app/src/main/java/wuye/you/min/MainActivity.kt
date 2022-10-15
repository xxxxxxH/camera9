package wuye.you.min

import android.Manifest
import android.content.Intent
import android.os.CountDownTimer
import android.view.View
import com.hjq.permissions.XXPermissions
import wuye.you.min.base.BaseActivity
import wuye.you.min.databinding.ActivityMainBinding
import wuye.you.min.utils.*

class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)
    private var isShowAd = false
    override fun initialization() {
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
                        startActivity(Intent(this, HomeActivity::class.java))
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

}