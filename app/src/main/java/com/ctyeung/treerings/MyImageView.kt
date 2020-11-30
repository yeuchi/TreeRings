package com.ctyeung.treerings

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.ImageView
import com.ctyeung.treerings.data.SharedPref

class MyImageView :androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {

    }

    private var screenWidth = 0
    private var screenHeight = 0

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        SharedPref.setImageViewSize(this.measuredWidth, this.measuredHeight)
    }
}