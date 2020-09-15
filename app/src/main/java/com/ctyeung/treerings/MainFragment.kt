package com.ctyeung.treerings

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.ctyeung.treerings.databinding.FragmentMainBinding
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentMainBinding;
    val READ_EXTERNAL_STORAGE_REQUEST = 0x1045 // not used
    val DELETE_PERMISSION_REQUEST = 0x1033
    val TRASH_PERMISSION_REQUEST = 0x1034
    val WRITE_PERMISSION_REQUEST = 0x00000002

    var photoURI: Uri?= null
    var currentPhotoPath: String?=null
    lateinit var photoStore:PhotoStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.layout = this;
        return binding!!.root;
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    public fun onClickConfig() {

    }

    public fun onClickCamera() {
        (this.activity as MainActivity).invokeCamera()
    }

    public fun onClickGallery() {

    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            handleTakePhoto(data);
        }
        else {
            Toast.makeText(this.context, "resultCode NOT OK", Toast.LENGTH_LONG).show()
        }
    }

    fun handleTakePhoto(data: Intent?) {
        var imageBitmap: Bitmap?=null

        if (data != null && data?.extras != null) {
            imageBitmap = data?.extras?.get("data") as Bitmap
            // TODO - specify preview
            //    binding?.layout?.imageView?.setImageBitmap(imageBitmap!!)
            photoStore.setNames("hello", "goldBucket")
            val returned = photoStore.save(imageBitmap)

            if(returned != "")
                Toast.makeText(this.context, returned, Toast.LENGTH_LONG).show()
        }
        else if(currentPhotoPath!=null) {
            // TODO - specify preview
            //   photoStore.read(currentPhotoPath!!, imageView)
            galleryAddPic(currentPhotoPath!!)
        }
        //revokeUriPermission(photoURI, Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    }

    private fun galleryAddPic(photoPath:String) {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(photoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            this.activity?.sendBroadcast(mediaScanIntent)
        }
    }
}