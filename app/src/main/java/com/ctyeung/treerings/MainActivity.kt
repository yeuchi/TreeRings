package com.ctyeung.treerings

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    external fun imageConvolveFromJNI(bmpIn: Bitmap?, BmpOut: Bitmap?, kernel: IntArray?, kernelWidth: Int, threshold: Int)

}
