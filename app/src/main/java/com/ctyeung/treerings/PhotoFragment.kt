package com.ctyeung.treerings

import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ctyeung.treerings.databinding.FragmentPhotoBinding
import kotlinx.android.synthetic.main.fragment_photo.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * TODO: Add tutorial dialog
 */
class PhotoFragment : Fragment(), IOnBackPressed {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var points = ArrayList<PointF>()
    private lateinit var binding: FragmentPhotoBinding
    private lateinit var photoStore: PhotoStorage
    private var canvas: Canvas? = null
    private lateinit var paper: MyPaperView

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo, container, false)
        binding.layout = this;
        return binding!!.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).enableBackButton(true)
        (activity as MainActivity).setTittle("Draw Line")
        this.paper = MyPaperView(this.requireContext())
        binding!!.layout!!.line_container!!.addView(this.paper)

        val uriString = arguments?.getString("url")
        if (uriString != null) {
            val photoUri: Uri? = Uri.parse(uriString)
            loadPhoto(photoUri)
        }
    }

    private fun loadPhoto(photoUri: Uri?) {
        if (photoUri != null)
            photoStore.read(
                requireActivity().contentResolver,
                photoUri,
                binding!!.layout!!.photo_view
            )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PhotoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PhotoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onBackPressed(): Boolean {
        binding!!.root.findNavController().navigate(R.id.action_photoFragment_to_mainFragment)
        return true
    }

    fun onClickNext() {
        if(this.paper.hasLine()) {
            val str = photoStore.imageUri.toString() ?: ""
            var bundle = bundleOf("url" to str)
            binding!!.root.findNavController().navigate(R.id.action_photoFragment_to_detailFragment, bundle)
        }
    }
}