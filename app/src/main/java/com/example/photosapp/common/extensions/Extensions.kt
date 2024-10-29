package com.example.photosapp.common.extensions

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.isValidLogin(): Boolean {
    val loginPattern = "^[a-z0-9_\\-.@]+$".toRegex()
    return this.length in 4..32 && this.matches(loginPattern)
}

fun String.isValidPassword(): Boolean {
    return this.length in 8..500
}

fun Bitmap.toBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun Int.formatToDate(): String {
    val milliseconds = this.toLong() * 1000
    val date = Date(milliseconds)
    val dateFormat = SimpleDateFormat("MM.dd.yyyy", Locale.getDefault())
    return dateFormat.format(date)
}
