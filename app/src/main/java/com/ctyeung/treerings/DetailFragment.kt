package com.ctyeung.treerings

import android.app.AlertDialog
import android.graphics.Point
import android.graphics.PointF
import android.net.Uri
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.ctyeung.treerings.data.SharedPref
import com.ctyeung.treerings.databinding.FragmentDetailBinding
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.android.synthetic.main.fragment_photo.line_container
import kotlinx.android.synthetic.main.fragment_photo.photo_view
import java.util.ArrayList

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
class DetailFragment : BaseFragment(), IOnNewLine {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding:FragmentDetailBinding
    protected var lineIntersects:IntArray?=null

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

        (activity as MainActivity).setTittle("Draw Line")
        binding!!.layout!!.line_container!!.addView(this.paper)

        if(photoUri != null) {
            loadPhoto(binding!!.layout!!.photo_view, photoUri)
            // TODO display an alertDialog for tutorial

            val size = getScreenSize()
            this.paper.demoLine(size.first, size.second)
            this.paper.callback = this

            showAlertDialog()
        }
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle("Count Rings")
            .setMessage("Draw an intersecting line across rings to count.")
            .create()
            .show()
    }

    private fun getScreenSize():Pair<Int, Int> {

        val displayMetrics = DisplayMetrics()
        this.activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return Pair<Int, Int> (displayMetrics.widthPixels, displayMetrics.heightPixels)
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

    /*
     * new line to compute intersections
     */
    override fun onNewLine(points:ArrayList<PointF>) {
        val size = SharedPref.getBitmapSize()
        val maxLen = Math.sqrt((size.first * size.first + size.second * size.second).toDouble()).toInt()
        this.lineIntersects = IntArray(maxLen)

        if (photoStore.bmp != null) {
            // find intersections
            (this.activity as MainActivity).intersect(photoStore.bmp!!, lineIntersects, points)

            // update line on screen with intersection highlight
            this.paper.drawIntersects(lineIntersects)
        }
    }
}