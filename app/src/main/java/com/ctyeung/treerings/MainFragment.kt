package com.ctyeung.treerings

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
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.ctyeung.treerings.data.PhotoStorage
import com.ctyeung.treerings.data.SharedPref
import com.ctyeung.treerings.databinding.FragmentMainBinding
import com.ctyeung.treerings.img.BitmapUtils

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * TODO: Add tutorial dialog
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

    lateinit var photoStore: PhotoStorage

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
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.layout = this;
        return binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).enableBackButton(false)
        (activity as MainActivity).setTittle("Select Photo")
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
        if(photoStore.imageUri != null) {
            val str = photoStore.imageUri.toString() ?: ""
            val bundle = bundleOf("url" to str)
            binding.root.findNavController().navigate(R.id.action_mainFragment_to_photoFragment, bundle)
            return
        }
        Toast.makeText(this.context, "Select photo first", Toast.LENGTH_LONG).show()
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
        val photoUri:Uri = data?.data!!;
        if(photoUri != null) {
            photoStore.read(requireActivity().contentResolver,
                            photoUri,
                            binding.photoPreview)

            SharedPref.setBitmapSize(photoStore.bmp?.width?:0, photoStore.bmp?.height?:0)
            SharedPref.setFilePath(SharedPref.keySrcFilePath, photoStore.imageUri.toString())
            return
        }
        Toast.makeText(this.context, "photoUri is null", Toast.LENGTH_LONG).show()
    }

    fun handleTakePhoto(data: Intent?) {
        var bmp: Bitmap?=null

        if (data != null && data.extras != null) {
            bmp = data.extras?.get("data") as Bitmap
            val context = this.context
            bmp?.apply {
                val bitmap = BitmapUtils.setPortrait(this)

                binding.photoPreview.setImageBitmap(bitmap)
                photoStore.setNames("original", "treerings")
                val returned = photoStore.save(bitmap)

                if(returned != "")
                    Toast.makeText(context, returned, Toast.LENGTH_LONG).show()

                SharedPref.setBitmapSize(bitmap.width?:0, bitmap.height?:0)
                SharedPref.setFilePath(SharedPref.keySrcFilePath, photoStore.imageUri.toString())
                return
            }
        }
        Toast.makeText(this.context, "data is null", Toast.LENGTH_LONG).show()
    }
}