package com.ctyeung.treerings

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.PointF
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ctyeung.treerings.data.SharedPref
import com.ctyeung.treerings.img.Kernel

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setHomeButtonEnabled(true)

        // Example of a call to a native method
        // sample_text.text = stringFromJNI()
    }

    fun enableBackButton(show:Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(show)
    }

    fun setTittle(str:String) {
        supportActionBar?.setTitle(str)
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    // external fun stringFromJNI(): String

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }

    /*
     * https://stackoverflow.com/questions/4939266/android-bitmap-native-code-linking-problem
     * need to set option in CMakeLists.txt
     */
    fun convolve(kernel: Kernel, bmpIn:Bitmap, bmpOut:Bitmap, threshold:Int) {
        try {
//            val bmpIn: Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_lion_small)
//            val bmpOut: Bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.white_lion_small_gray)
            imageConvolveFromJNI(bmpIn, bmpOut, kernel.mValues, kernel.mWidth, threshold)

        } catch (ex: java.lang.Exception) {
            Toast.makeText(this,
                ex.toString() as String,
                Toast.LENGTH_LONG).show()
        }
    }
    external fun imageConvolveFromJNI(bmpIn: Bitmap?,
                                      BmpOut: Bitmap?,
                                      kernel: IntArray?,
                                      kernelWidth: Int,
                                      threshold: Int)

    fun intersect(bmp:Bitmap,
                  lineIntersets: IntArray?,
                  points:ArrayList<PointF>) {
        /*
         * scale screen size to bitmap size
         */
        val bmpSize = SharedPref.getBitmapSize()
        val imgSize = SharedPref.getImageViewSize()
        val xRatio = bmpSize.first.toDouble() / imgSize.first.toDouble()
        val yRatio = bmpSize.second.toDouble() / imgSize.second.toDouble()
        var x0 = (points[0].x.toDouble() * xRatio).toInt()
        var y0 = (points[0].y.toDouble() * yRatio).toInt()
        var x1 = (points[1].x.toDouble() * xRatio).toInt()
        var y1 = (points[1].y.toDouble() * yRatio).toInt()

        /*
         * For now, assume vertical ish line
         */
        if(y1 < y0) {
            val x = x1
            val y = y1

            x1 = x0
            y1 = y0
            x0 = x
            y0 = y
        }

        imageFindIntersectsFromJNI( bmp,
            lineIntersets,
            lineIntersets!!.size,
            x0, y0,
            x1, y1)
    }

    external fun imageFindIntersectsFromJNI(bmpIn: Bitmap?, lineIntersets:IntArray?, len:Int, x0:Int, y0:Int, x1:Int, y1:Int)
}
