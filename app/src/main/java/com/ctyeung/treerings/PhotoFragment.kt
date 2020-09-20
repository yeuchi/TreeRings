package com.ctyeung.treerings

import android.graphics.*
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
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
 * A simple [Fragment] subclass.
 * Use the [PhotoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PhotoFragment : Fragment(), IOnBackPressed {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var points = ArrayList<PointF>()
    private lateinit var binding: FragmentPhotoBinding
    private lateinit var photoStore:PhotoStorage
    private var canvas:Canvas? = null

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

        val uriString = arguments?.getString("url")
        if(uriString != null) {
            val photoUri: Uri? = Uri.parse(uriString)
            loadPhoto(photoUri)

            if(photoStore.bmp != null) {
                val config=Bitmap.Config.ARGB_8888
                val sRgb = ColorSpace.get(ColorSpace.Named.SRGB)
                val w = photoStore!!.bmp!!.width
                val h = photoStore!!.bmp!!.height
                var bitmap = Bitmap.createBitmap(w,h, config, true, sRgb)
                canvas = Canvas(bitmap)
            }
        }
        addTouchEvent()
    }

    private fun loadPhoto(photoUri:Uri?) {
        if(photoUri != null)
            photoStore.read(requireActivity().contentResolver, photoUri, binding!!.layout!!.photoView)
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

    /*
     * Draw line base on touch
     */
    private fun addTouchEvent() {
        lineView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        onTouchDown(PointF(event.x, event.y))
                    }

                    MotionEvent.ACTION_UP -> {
                        // do nothing
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
    }

    private fun onTouchDown(p:PointF) {

        when(points.size) {
            0 -> {      // draw a point
                drawPoint(p)
                points.add(p)
            }

            1 -> {      // draw a line
                drawLine(points[0], p)
                drawPoint(p)
                points.add(p)
            }

            else -> {   // 2 is the max supported - find nearest, replace, draw line
                updateLine(p)
            }
        }
        lineView.invalidate()
    }

    private fun drawPoint(p:PointF) {
        val paint = Paint()
        paint.setColor(Color.BLUE)
        canvas?.drawCircle(p.x, p.y, 1F, paint);
    }

    private fun drawLine(p1:PointF, p2:PointF) {
        val paint = Paint()
        paint.setColor(Color.BLUE)
        canvas?.drawLine(p1.x, p1.y, p2.x, p2.y, paint)
    }

    /*
     * 1. Find/replace nearest point
     * 2. Draw new line
     */
    private fun updateLine(p:PointF) {
        val dis1 = distance(points[0], p)
        val dis2 = distance(points[1], p)

        if(dis1 < dis2) {
            points[0] = p
        }
        else {
            points[1] = p
        }
        drawLine(points[0], points[1])
        drawPoint(points[0])
        drawPoint(points[1])
    }

    private fun distance(p1:PointF, p2:PointF):Double {
        val dx:Double = (p2.x as Double)-(p1.x as Double)
        val dy:Double = (p2.y as Double)-(p1.y as Double)
        val diff = dx*dx + dy*dy
        return Math.sqrt(diff)
    }
}