package com.example.photosapp.ui.splash

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.photosapp.common.base.BaseFragment
import com.example.photosapp.databinding.FragmentSplashBinding

class SplashScreen : BaseFragment<FragmentSplashBinding>() {
    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSplashBinding = FragmentSplashBinding.inflate(inflater, container, false)
}