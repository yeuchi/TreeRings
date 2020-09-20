package com.ctyeung.treerings

import android.graphics.Bitmap
import android.graphics.Matrix

object BitmapUtils {

     fun setPortrait(bmp: Bitmap): Bitmap {
        if(bmp.width > bmp.height) {
            val matrix = Matrix()
            matrix.postRotate(90F)
            return Bitmap.createBitmap(bmp, 0,0, bmp.width, bmp.height, matrix, true)
        }
        return bmp
    }
}