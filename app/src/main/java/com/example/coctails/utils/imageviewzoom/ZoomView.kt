package com.example.coctails.utils.imageviewzoom

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView


class ZoomView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    ImageView(context, attrs, defStyleAttr) {
    var attacher: ZoomViewAttacher = ZoomViewAttacher(this)

}
