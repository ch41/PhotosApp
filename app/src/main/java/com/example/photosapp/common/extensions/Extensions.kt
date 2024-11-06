package com.example.photosapp.common.extensions

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.util.Base64
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
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

fun Bitmap.toBase64(compressFormat: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 80): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(compressFormat, quality, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun Int.formatToDate(): String {
    val milliseconds = this.toLong() * 1000
    val date = Date(milliseconds)
    val dateFormat = SimpleDateFormat("MM.dd.yyyy", Locale.getDefault())
    return dateFormat.format(date)
}

fun Activity.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val token = view.windowToken
    inputMethodManager.hideSoftInputFromWindow(token, 0)
}

fun Bitmap.resizeToMaxSize(maxSize: Int): Bitmap {
    val aspectRatio: Float = this.width.toFloat() / this.height.toFloat()
    val newWidth: Int
    val newHeight: Int

    if (this.width > this.height) {
        newWidth = maxSize
        newHeight = (newWidth / aspectRatio).toInt()
    } else {
        newHeight = maxSize
        newWidth = (newHeight * aspectRatio).toInt()
    }

    return Bitmap.createScaledBitmap(this, newWidth, newHeight, false)
}

fun Bitmap.toBase64String(format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, quality: Int = 100): String {
    val outputStream = ByteArrayOutputStream()
    this.compress(format, quality, outputStream)
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}
fun Bitmap.resizeAndRotate(photoUri: Uri, context: Context, maxSize: Int): Bitmap {

    val resizedBitmap = this.resizeToMaxSize(maxSize)

    val inputStream: InputStream = context.contentResolver.openInputStream(photoUri)!!
    val exifInterface = ExifInterface(inputStream)

    val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)

    return when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> resizedBitmap.rotate(90f)
        ExifInterface.ORIENTATION_ROTATE_180 -> resizedBitmap.rotate(180f)
        ExifInterface.ORIENTATION_ROTATE_270 -> resizedBitmap.rotate(270f)
        else -> resizedBitmap
    }
}

fun Bitmap.rotate(degrees: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(degrees)
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
}