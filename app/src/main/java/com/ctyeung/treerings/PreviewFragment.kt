package com.ctyeung.treerings

import android.content.Context
import android.graphics.*
import android.hardware.Camera
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

/*
 * 1. take photo
 * 2. user draws line
 * 3. modulation transfer function to determine the quality of photo
 *
 */

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PreviewFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PreviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class PreviewFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private var downx = 0f
    private var downy = 0f
    private var upx = 0f
    private var upy = 0f
    private var imageView:ImageView?=null
    private var mPreview:CameraPreview?=null
    private var canvas:Canvas?=null
    private lateinit var bmp:Bitmap
    private lateinit var mPaint:Paint

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
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_preview, container, false)

        mPreview = CameraPreview(context)
        if (null != rootView) {
            val frame = rootView as FrameLayout
            rootView.addView(mPreview)
        }
        initImageView(rootView)
        initButtons(rootView)
        return view;
    }

    protected fun drawLine(pt0: PointF, pt1: PointF) {

        canvas?.drawBitmap(bmp, Matrix(), null)
        canvas?.drawLine(pt0.x, pt0.y, pt1.x, pt1.y, mPaint)

        imageView?.invalidate()
    }

    fun initImageView(rootView:View) {
        var imageView = rootView.findViewById(R.id.imageView1) as ImageView
        imageView.setOnTouchListener(View.OnTouchListener { v, event ->
            if (bmp == null)
                return@OnTouchListener false

            // TODO Auto-generated method stub
            val action = event.action
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    downx = event.x
                    downy = event.y
                }
                MotionEvent.ACTION_MOVE -> {
                }
                MotionEvent.ACTION_UP -> {
                    upx = event.x
                    upy = event.y

                    drawLine(PointF(downx, downy), PointF(upx, upy))

                }

            }
            true
        })
    }

    fun initButtons(rootView: View) {
        // capture image
        val button_capture = rootView.findViewById(R.id.button_capture) as Button
        button_capture.bringToFront()

        button_capture.setOnClickListener {
            mPreview?.capture(Camera.PictureCallback { data, camera ->
                //http://stackoverflow.com/questions/17022221/openfileoutput-how-to-create-files-outside-the-data-data-path
                val fos: FileOutputStream

                try {
                    val folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                    val df = SimpleDateFormat("d MMM yyyy HH:mm:ss")
                    val date = df.format(Calendar.getInstance().time)
                    val path = folder.absolutePath + "/" + date + ".png"
                    fos = FileOutputStream(path)
                    bmp = BitmapFactory.decodeByteArray(data, 0, data.size)
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
                    fos.close()

                    val matrix = Matrix()
                    matrix.postRotate(90f)

                    var w:Int = bmp.getWidth()
                    var h:Int = bmp.getHeight()

                    bmp = Bitmap.createBitmap(
                        bmp, 0, 0, w,
                        h, matrix, true
                    )

                    val bmpSrc = Bitmap.createBitmap(
                        bmp, 0, 0, bmp.getWidth(),
                        bmp.getHeight(), Matrix(), true
                    )
                    if (bmpSrc != null) {
                        canvas = Canvas(bmpSrc)
                        mPaint = Paint()
                        mPaint.setColor(Color.GREEN)
                    }
                    imageView?.setImageBitmap(bmpSrc)

                } catch (e: Exception) {
                    Log.e("Still", "error writing file", e)
                }
            })
        }

        // clear captured button
        val button_clear = rootView.findViewById(R.id.button_clear) as Button
        button_clear.bringToFront()
        button_clear.setOnClickListener {
            // TODO Auto-generated method stub
            imageView?.setImageBitmap(null)
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PreviewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PreviewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
