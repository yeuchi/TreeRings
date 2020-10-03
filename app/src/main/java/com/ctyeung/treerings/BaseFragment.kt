package com.ctyeung.treerings

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.ctyeung.treerings.data.PhotoStorage

open class BaseFragment : Fragment(), IOnBackPressed {

    protected lateinit var photoStore: PhotoStorage
    protected lateinit var paper: MyPaperView
    protected var photoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoStore = PhotoStorage(this.requireActivity().applicationContext)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).enableBackButton(true)
        this.paper = MyPaperView(this.requireContext())

        val uriString = arguments?.getString("url")
        if (uriString != null) {
            photoUri = Uri.parse(uriString)
        }
    }

    fun loadPhoto(imageView:ImageView,photoUri: Uri?) {
        if (photoUri != null)
            photoStore.read(
                requireActivity().contentResolver,
                photoUri,
                imageView
            )
    }

    override fun onBackPressed(): Boolean {
        TODO("Not yet implemented")
    }
}