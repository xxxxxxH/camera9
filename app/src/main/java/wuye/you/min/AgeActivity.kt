package wuye.you.min

import android.graphics.Bitmap
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import wuye.you.min.base.BaseActivity
import wuye.you.min.databinding.ActivityAgeBinding

class AgeActivity : BaseActivity<ActivityAgeBinding>(), View.OnClickListener {

    override fun getViewBinding() = ActivityAgeBinding.inflate(layoutInflater)

    override fun initialization() {
        intent.getStringExtra("imageUrl")?.let {
            Glide.with(this).load(it).into(activityBinding.agePv)
        }
        activityBinding.cancel.setOnClickListener(this)
        activityBinding.save.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.cancel -> finish()
            R.id.save -> finish()
        }
    }
}