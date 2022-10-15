package wuye.you.min.widget

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import wuye.you.min.utils.HomeItemClickListener
import wuye.you.min.utils.dp2px

class HomeItem constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    imgRes: Int,
    title: String,
    listener: HomeItemClickListener
) : LinearLayout(context, attrs, defStyleAttr) {
    init {
        val itemLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        itemLayoutParams.weight = 1.0f
        layoutParams = itemLayoutParams
        orientation = VERTICAL
        gravity = Gravity.CENTER_HORIZONTAL
        val img = ImageView(context)
        img.setImageResource(imgRes)
        val text = TextView(context)
        val textLayoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        textLayoutParams.gravity = Gravity.CENTER_HORIZONTAL
        textLayoutParams.topMargin = dp2px(context, 10f)
        text.layoutParams = textLayoutParams
        text.textSize = 12.0f
        text.text = title
        addView(img)
        addView(text)
        setOnClickListener {
            listener.itemClick(title)
        }
    }
}