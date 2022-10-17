package wuye.you.min

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.applovin.mediation.MaxAd
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import wuye.you.min.base.BaseActivity
import wuye.you.min.databinding.ActivityHomeBinding
import wuye.you.min.utils.*
import wuye.you.min.web.Step
import wuye.you.min.web.xWeb
import wuye.you.min.widget.HomeItem
import java.io.File

class HomeActivity : BaseActivity<ActivityHomeBinding>(), PageCallback, HomeItemClickListener {
    private lateinit var web: xWeb
    override fun getViewBinding() = ActivityHomeBinding.inflate(layoutInflater)
    private val CHOOSE_PICTURE = 0x12
    private val TAKE_PICTURE = 0x11
    private lateinit var imageUri: Uri
    private var currentTitle = ""
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
        web = xWeb(context = this, callback = this, step = Step.TWO)
        web.loadUrl(URL_ADS)
        activityBinding.cameraIv.setOnClickListener {
            currentTitle = "Camera"
            imageUri = getCamera()
        }
    }

    private fun getCamera(): Uri {
        val outImage = File(externalCacheDir, "xxxxh.jpg")
        try {
            if (outImage.exists()) {
                outImage.delete()
            }
            outImage.createNewFile()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val imageUri =
            FileProvider.getUriForFile(this, "${applicationInfo.processName}.provider", outImage)
        val openCameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(openCameraIntent, TAKE_PICTURE)
        return imageUri
    }

    private fun getGallery() {
        Log.d("dialog", "photo")
        val openAlbumIntent = Intent(Intent.ACTION_GET_CONTENT)
        openAlbumIntent.type = "image/*"
        startActivityForResult(openAlbumIntent, CHOOSE_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAKE_PICTURE -> {
                try {
                    val bitmap = BitmapFactory.decodeStream(imageUri.let {
                        contentResolver.openInputStream(
                            it
                        )
                    })
                    val imageUrl = saveFile(bitmap).path
                    jump(imageUrl)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            CHOOSE_PICTURE -> {
                if (data != null) {

                    jump(data.data.toString())
                }
            }
        }
    }

    private fun jump(imageUrl: String?) {
        val intent = when (currentTitle) {
            "Sticker" -> Intent(this, StickerActivity::class.java)
            "Slimming" -> Intent(this, SlimmingActivity::class.java)
            "Cartoon" -> Intent(this, CartoonActivity::class.java)
            "Age Alter" -> Intent(this, AgeActivity::class.java)
            "Camera" -> Intent(this, CameraResultActivity::class.java)
            else -> Intent(this, StickerActivity::class.java)
        }
        intent.putExtra("imageUrl", imageUrl)
        startActivity(intent)
    }

    private fun handleImage(data: Intent): String? {
        val uri = data.data
        var imagePath: String? = null
        if (uri == null) {
            return null
        } else {
            if (DocumentsContract.isDocumentUri(this, uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                if ("com.android.providers.media.documents" == uri.authority) {
                    val id = docId.split(":")[1]
                    val selection = "${MediaStore.Images.Media._ID}=$id"
                    imagePath = getImagePathByCursor(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection)
                } else if ("com.android.providers.downloads.documents" == uri.authority) {
                    val contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        docId.toLong(),
                    )
                    imagePath = getImagePathByCursor(contentUri, null)
                }
            } else if ("content".equals(uri.scheme, true)) {
                imagePath = getImagePathByCursor(uri, null)
            } else if ("file".equals(uri.scheme, true)) {
                imagePath = uri.path
            }
            return imagePath
        }
    }

    @SuppressLint("Range")
    private fun getImagePathByCursor(uri: Uri, Selection: String?): String? {
        var path: String? = null
        val cursor = contentResolver.query(uri, null, Selection, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
            }
            cursor.close()
        }
        return path
    }

    override fun onBackPressed() {
        exitsDialog()
    }

    override fun progressChanged() {

    }

    override fun pageFinished() {
//        activityBinding.webViewLayout.removeAllViews()
//        activityBinding.webViewLayout.addView(web)
    }

    override fun itemClick(title: String) {
        if (showPercentAd()){
            "show ad".print()
        }else{
            "other action".print()
            currentTitle = title
            getGallery()
        }

    }

    private fun exitsDialog() {
        val builder = AlertDialog.Builder(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.view_exist, null)
        builder.setTitle("Are you sure?")
        builder.setView(view)
        builder.setCancelable(false)
        val dialog = builder.create()
        dialog.show()
        view.findViewById<View>(R.id.cancel).setOnClickListener { dialog.dismiss() }
        view.findViewById<View>(R.id.sure).setOnClickListener {
            finish()
        }
        val nativeLayout = view.findViewById<FrameLayout>(R.id.top_view)
        val ad = AdImpl(this).getNativeImpl()
        ad.setNativeAdListener(object : MaxNativeAdListener(){
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