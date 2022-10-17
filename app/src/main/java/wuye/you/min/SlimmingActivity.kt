package wuye.you.min

import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import wuye.you.min.base.BaseActivity
import wuye.you.min.databinding.ActivitySlimmingBinding

class SlimmingActivity : BaseActivity<ActivitySlimmingBinding>() {

    override fun getViewBinding() = ActivitySlimmingBinding.inflate(layoutInflater)

    override fun initialization() {
        intent.getStringExtra("imageUrl")?.let {
            Glide.with(this).load(it).into(activityBinding.slimmingImage)
        }
        activityBinding.cancel.setOnClickListener { finish() }
        activityBinding.save.setOnClickListener { finish() }
    }
}