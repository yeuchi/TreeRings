package com.ctyeung.treerings

import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
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
 * TODO:
 * 1. Add tutorial dialog
 * 2. Add a zoom / pan
 */
class PhotoFragment : BaseFragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentPhotoBinding

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_photo, container, false)
        binding.layout = this;
        return binding!!.root;
    }

    protected var bmpIn:Bitmap? = null
    protected var bmpOut:Bitmap? = null
    protected var kernel:Kernel? = null
    protected var thresholdValue:Int = 30

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).setTittle("Processing")
        binding!!.layout!!.line_container!!.addView(this.paper)

        if(photoUri != null) {
            bmpIn = photoStore.load(requireActivity().contentResolver, photoUri!!)
            if(bmpIn != null) {
                this.kernel = KernelFactory.verticalDerivative()
                this.bmpOut = BitmapUtils.create(bmpIn!!)
                val photoEdges = binding!!.layout!!.photo_view
                photoEdges.setImageBitmap(bmpOut)
                detectEdges(thresholdValue)
            }
        }
        handleSeekBar()
    }

    /*
     * TODO - There is a bug in my bmp indexing.
     *  If I don't reload bmpIn, bmpOut is rendered incorrectly..
     * Looks like I am writing over bmpIn somehow.
     */
    fun detectEdges(thresholdValue:Int){
        if(this.kernel!=null && this.bmpIn!=null ) {
            bmpIn = photoStore.load(requireActivity().contentResolver, photoUri!!)
            //val bitmap = Bitmap.createScaledBitmap(bmpIn!!, bmpIn!!.width, bmpIn!!.height, false)
            (this.activity as MainActivity).convolve(this.kernel!!, bmpIn!!, this.bmpOut!!, thresholdValue)
            return
        }
        Toast.makeText(this.context, "Can't detect edges", Toast.LENGTH_LONG).show()
    }

    fun handleSeekBar() {
        val seek = binding!!.layout!!.seekbar
        seek?.progress = this.thresholdValue
        seek?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int,
                                           fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                if(thresholdValue !== seek.progress) {
                    thresholdValue = seek.progress
                    txtSeekValue.text = seek.progress.toString()
                    detectEdges(thresholdValue)
                }
            }
        })
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
        if(bmpOut != null) {
            photoStore.setNames("edges", "treerings")
            val returned = photoStore.save(bmpOut!!)
            val str = photoStore.imageUri.toString() ?: ""
            var bundle = bundleOf("url" to str)
            binding!!.root.findNavController()
                .navigate(R.id.action_photoFragment_to_detailFragment, bundle)
        }
    }
}