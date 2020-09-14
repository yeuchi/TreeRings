package com.ctyeung.treerings

import android.databinding.DataBindingUtil
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ctyeung.treerings.databinding.FragmentMainBinding;

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Fragment() {

    private lateinit var binding:FragmentMainBinding;

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        return view;
    }

    public fun onClickConfig() {

    }

    public fun onClickCamera() {

    }

    public fun onClickGallery() {

    }
}
