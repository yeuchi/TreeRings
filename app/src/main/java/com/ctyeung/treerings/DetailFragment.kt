package com.ctyeung.treerings

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.ctyeung.treerings.databinding.FragmentDetailBinding
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.android.synthetic.main.fragment_photo.line_container
import kotlinx.android.synthetic.main.fragment_photo.photo_view

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/*
 * Perform image processing and count on this page
 * 1. start with a magnified section.
 * 2. perform the image processing -- allow user to adjust sensitivity.
 * 3. user selects and count !
 */
class DetailFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding:FragmentDetailBinding


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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.layout = this;
        return binding!!.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setTittle("Count Rings")
        binding!!.layout!!.line_container!!.addView(this.paper)

        if(photoUri != null) {
            val bmpIn = photoStore.load(requireActivity().contentResolver, photoUri!!)
            if(bmpIn != null) {

                var bmpOut = BitmapUtils.create(bmpIn)
                val kernel = KernelFactory.XYDerivative()

                (this.activity as MainActivity).convolve(kernel, bmpIn, bmpOut)
                val photoEdges = binding!!.layout!!.photo_view
                photoEdges.setImageBitmap(bmpOut)
            }

        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailFragment().apply {
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
}