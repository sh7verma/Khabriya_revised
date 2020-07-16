package com.sdei.khabriya.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sdei.khabriya.R

fun ImageView.loadImage(image: String?) {
    Glide.with(this).load(image).centerCrop()
        .placeholder(R.mipmap.no_bg_logo)
        .error(R.mipmap.no_bg_logo).into(this)
}

fun showSnackBar(view: View, message: String) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
}

fun showAlertSnackBar(view: View, message: String) {
    val mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
    mSnackbar.view.setBackgroundColor(Color.RED)
    mSnackbar.show()
}

fun Context.showSucessSnackBar(view: View, message: String, activity: Activity) {
    val mSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    mSnackbar.view.setBackgroundColor(Color.parseColor("#32CD32"))
    mSnackbar.setAction("OK") {
        activity.finish()
    }.setActionTextColor(Color.WHITE).show()
}

fun Context.showToast(text: String?, duration: Int = Toast.LENGTH_SHORT) {
    text?.let { Toast.makeText(this, it, duration).show() }
}
