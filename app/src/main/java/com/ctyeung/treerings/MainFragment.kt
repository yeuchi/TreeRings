package com.ctyeung.treerings

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.ctyeung.treerings.databinding.FragmentMainBinding
import kotlinx.android.synthetic.main.fragment_main.*
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
    val REQUEST_TAKE_PHOTO = 1
    val PICK_PHOTO_CODE = 1046

    var photoURI: Uri?= null
    var currentPhotoPath: String?=null
    lateinit var photoStore:PhotoStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        photoStore = PhotoStorage(this.requireActivity().applicationContext)
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

    fun onClickCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_TAKE_PHOTO)
    }

    fun onClickGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_PHOTO_CODE)
    }

    fun onClickNext() {
        val str = photoStore.imageUri?:""
        var bundle = bundleOf("url" to str)
        binding!!.root.findNavController().navigate(R.id.action_mainFragment_to_photoFragment, bundle)
    }

    override fun onActivityResult(requestCode: Int,
                                  resultCode: Int,
                                  data: Intent?) {
        if(data != null) {
            when (requestCode) {

                REQUEST_TAKE_PHOTO -> handleTakePhoto(data);

                PICK_PHOTO_CODE -> handleLoadPhoto(data);

                else -> {
                    Toast.makeText(this.context, "requestCode NOT OK", Toast.LENGTH_LONG).show()
                }
            }
            return
        }
        Toast.makeText(this.context, "data missing", Toast.LENGTH_LONG).show()
    }

    fun handleLoadPhoto(data: Intent?){
        val photoUri:String? = data?.data.toString();
        if(photoUri != null)
            photoStore.read(photoUri, binding!!.layout!!.photoPreview)
    }

    fun handleTakePhoto(data: Intent?) {
        var bmp: Bitmap?=null

        if (data != null && data?.extras != null) {
            bmp = data?.extras?.get("data") as Bitmap

            if(bmp.width > bmp.height) {
                val matrix = Matrix()
                matrix.postRotate(90F)
                bmp = Bitmap.createBitmap(bmp, 0,0, bmp.width, bmp.height, matrix, true)
            }

            binding?.layout?.photoPreview?.setImageBitmap(bmp!!)
            photoStore.setNames("hello", "goldBucket")
            val returned = photoStore.save(bmp!!)

            if(returned != "")
                Toast.makeText(this.context, returned, Toast.LENGTH_LONG).show()
        }
        else if(currentPhotoPath!=null) {
            photoStore.read(currentPhotoPath!!, binding!!.layout!!.photoPreview)
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