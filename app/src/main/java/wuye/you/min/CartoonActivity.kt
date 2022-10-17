package wuye.you.min

import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import wuye.you.min.base.BaseActivity
import wuye.you.min.databinding.ActivityCartoonBinding

class CartoonActivity : BaseActivity<ActivityCartoonBinding>() {

    override fun getViewBinding(): ActivityCartoonBinding {
        return ActivityCartoonBinding.inflate(layoutInflater)
    }

    override fun initialization() {
        intent.getStringExtra("imageUrl")?.let {
            Glide.with(this).load(it).into(activityBinding.cartoonIv)
        }
        activityBinding.cancel.setOnClickListener { finish() }
        activityBinding.save.setOnClickListener { finish() }
    }
}