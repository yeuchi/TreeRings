package com.ctyeung.treerings.data

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import com.ctyeung.treerings.MainApplication
import java.io.File
import com.ctyeung.treerings.R

/*
 * TODO
 *  Shared Preference appears non functional for API 33.
 *  Should replace with DataStore.
 *  Anyhow - global variable works fine; only a single instance.
 */
object SharedPref {

    private val mypreference = "mypref"

//    val keyKernelType = "kernelType"
    var keyKernelType = ""
    fun getKernelType(key:String?= keyKernelType):String {
//        return getString(keyKernelType, "")
        return keyKernelType
    }

    /*
     * Bitmap file access
     * 1. source image
     * 2. processed threshold line image
     */
//    val keySrcFilePath = "srcfilepath"
//    val keyLineFilePath = "linefilepath"

    var keySrcFilePath = "default.txt"
    var keyLineFilePath = ""

    fun getFile(key:String?=keySrcFilePath): File {
        val path = Environment.getExternalStorageDirectory().toString()

        val directory = File(path, MainApplication.applicationContext().resources.getString(R.string.default_directory))
        if(!directory.exists())
            directory.mkdirs()

//        val filename = getFilePath(key)
        val file = File(directory, keySrcFilePath)
        return file
    }

    fun setFilePath(filePath:String, key:String?=keySrcFilePath)
    {
//        setString(key!!, filePath)
        keySrcFilePath = filePath
    }

    /*
     * ImageView screen dimension
     */
//    val keyImageViewWidth = "imageviewwidth"
//    val keyImageViewHeight = "imageviewheight"

    var keyImageViewWidth:Int = 0;
    var keyImageViewHeight:Int = 0;

    fun getImageViewSize():Pair<Int, Int> {
//        return Pair(getInt(keyImageViewWidth),
//            getInt(keyImageViewHeight))
        return Pair(keyImageViewWidth, keyImageViewHeight)
    }


    fun setImageViewSize(width:Int, height:Int) {
        keyImageViewWidth = width
        keyImageViewHeight = height;
//        val sharedPreferences =
//            getSharedPref(
//                MainApplication.applicationContext()
//            )
//        setInt(keyImageViewWidth, width)
//        setInt(keyImageViewHeight, height)
    }

    /*
     * Bitmap dimension
     */
//    val keyBmpWidth = "bmpwidth"
//    val keyBmpHeight = "bmpheight"

    var keyBmpWidth = 0
    var keyBmpHeight = 0

    fun getBitmapSize():Pair<Int, Int> {
//        return Pair(getInt(keyBmpWidth),
//            getInt(keyBmpHeight))

        return Pair(keyBmpWidth, keyBmpHeight)
    }

    fun setBitmapSize(width:Int, height:Int) {
//        val sharedPreferences =
//            getSharedPref(
//                MainApplication.applicationContext()
//            )
        keyBmpWidth = width
        keyBmpHeight = height
//        setInt(keyBmpWidth, width)
//        setInt(keyBmpHeight, height)
    }

//    fun getFilePath(key:String?=keySrcFilePath):String
//    {
//        return getString(
//            key!!,
//            MainApplication.applicationContext().resources.getString(R.string.default_filename)
//        )
//    }

//    private fun getInt(key:String):Int
//    {
//        val sharedPreferences =
//            getSharedPref(
//                MainApplication.applicationContext()
//            )
//        return sharedPreferences.getInt(key, 0)?:0
//    }
//
//    private fun setInt(key:String, value:Int)
//    {
//        val sharedPreferences =
//            getSharedPref(
//                MainApplication.applicationContext()
//            )
//        val editor = sharedPreferences.edit()
//        editor.putInt(key, value)
//        editor.commit()
//    }
//
//    private fun getString(key:String, defaultValue:String):String
//    {
//        val sharedPreferences =
//            getSharedPref(
//                MainApplication.applicationContext()
//            )
//        return sharedPreferences.getString(key, defaultValue)?:defaultValue
//    }
//
//    private fun setString(key:String, str:String)
//    {
//        val sharedPreferences =
//            getSharedPref(
//                MainApplication.applicationContext()
//            )
//        val editor = sharedPreferences.edit()
//        editor.putString(key, str)
//        editor.commit()
//    }
//
//    private fun getSharedPref(context: Context): SharedPreferences
//    {
//        return context.getSharedPreferences(mypreference, Context.MODE_PRIVATE)
//    }
}