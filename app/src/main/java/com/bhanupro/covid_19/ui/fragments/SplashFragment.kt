package com.bhanupro.covid_19.ui.fragments

import android.Manifest
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import com.bhanupro.covid_19.R
import com.bhanupro.covid_19.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.fragment_splash.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 * A simple [Fragment] subclass.
 */
class SplashFragment : Fragment() {

    private val viewModel:MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        startAnimation()
        viewModel.loadWorldData()
    }
    private fun startAnimation(){
        ObjectAnimator.ofFloat(logo, View.ROTATION, 0f, 360f).setDuration(15000).start();
    }
    private fun observeViewModel(){
        viewModel.getWorldData().observe(viewLifecycleOwner, Observer {
            if (it != null){
                navigateTo()
            }
        })
    }
    private fun navigateTo(){
        findNavController().navigate(R.id.splashFragment_to_homeFragment)
    }
    private fun requestStoragePermissions(){
        requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),101)
    }

}
