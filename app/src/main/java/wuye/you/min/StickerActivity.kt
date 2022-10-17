package wuye.you.min

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.applovin.mediation.MaxAd
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.bumptech.glide.Glide
import com.lcw.library.stickerview.Sticker
import com.lohas.library.KtBaseAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wuye.you.min.base.BaseActivity
import wuye.you.min.base.MyApp
import wuye.you.min.databinding.ActivityStickerBinding
import wuye.you.min.utils.AdImpl
import wuye.you.min.utils.ResourceBean

class StickerActivity : BaseActivity<ActivityStickerBinding>() {

    override fun getViewBinding() = ActivityStickerBinding.inflate(layoutInflater)

    override fun initialization() {
        activityBinding.recycler.layoutManager = GridLayoutManager(this, 4)
        val list = getStickerList().toList()
        val adapter = StickerAdapter(list, R.layout.item_stickers)
        adapter.setItemClickListener(object : KtBaseAdapter.ItemClick {
            override fun OnItemClick(v: View, position: Int) {
                val bitmap = BitmapFactory.decodeResource(resources, list[position].id, null)
                val sticker = Sticker(this@StickerActivity, bitmap)
                activityBinding.stickerView.addSticker(sticker)
            }
        })
        activityBinding.recycler.adapter = adapter
        activityBinding.save.setOnClickListener { saveDialog() }
        intent.getStringExtra("imageUrl")?.let {
            Glide.with(this).load(it).into(activityBinding.showEditIv)
        }
        activityBinding.cancel.setOnClickListener { finish() }

    }

    private fun getStickerList(): List<ResourceBean> {
        val result = ArrayList<ResourceBean>()
        for (field in R.mipmap::class.java.fields) {
            val name = field.name
            if (name.startsWith("sticker")) {
                val id = MyApp.instance.resources.getIdentifier(
                    name,
                    "mipmap",
                    MyApp.instance.packageName
                )
                val entity = ResourceBean(name, id)
                result.add(entity)
            }
        }
        return result
    }

    class StickerAdapter : KtBaseAdapter<ResourceBean> {
        constructor(mList: List<ResourceBean>?, mLayoutId: Int?) : super(mList, mLayoutId)

        override fun convert(itemView: View?, item: ResourceBean) {
            itemView?.findViewById<ImageView>(R.id.item_sticker_iv)?.setImageResource(item.id)
        }
    }

    private fun saveDialog() {
        val builder = AlertDialog.Builder(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.view_exist, null)
        builder.setTitle("Are you sure to save ?")
        builder.setView(view)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
        view.findViewById<View>(R.id.cancel).setOnClickListener { dialog.dismiss() }
        view.findViewById<View>(R.id.sure).setOnClickListener {
            dialog.setTitle("Loading...")
            lifecycleScope.launch(Dispatchers.IO) {
                delay(3500)
                withContext(Dispatchers.Main) {
                    dialog.dismiss()
                    Toast.makeText(this@StickerActivity, "save success!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
        val nativeLayout = view.findViewById<FrameLayout>(R.id.top_view)
        val ad = AdImpl(this).getNativeImpl()
        ad.setNativeAdListener(object : MaxNativeAdListener() {
            override fun onNativeAdLoaded(p0: MaxNativeAdView?, p1: MaxAd?) {
                super.onNativeAdLoaded(p0, p1)
                p0?.let {
                    nativeLayout.removeAllViews()
                    nativeLayout.addView(it)
                }
            }
        })
    }
}