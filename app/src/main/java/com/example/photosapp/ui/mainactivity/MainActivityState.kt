package com.example.photosapp.ui.mainactivity

data class MainActivityState(
    val success:Boolean = true,
    val errorText:String? = null,
    val loading:Boolean = false,
    val hasError:Boolean = false
)