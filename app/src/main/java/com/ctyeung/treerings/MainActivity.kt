package com.ctyeung.treerings

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    val REQUEST_TAKE_PHOTO = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        for (fragment in supportFragmentManager.fragments) {

            when(requestCode) {
                REQUEST_TAKE_PHOTO -> {
                    fragment.onActivityResult(requestCode, resultCode, data)
                }

                else -> {
                    Toast.makeText(this, "bad request code", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun invokeCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Don't call resolve in Android 11, API 30
        // if (takePictureIntent.resolveActivity(packageManager) != null) {
        startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
        // }
    }
}