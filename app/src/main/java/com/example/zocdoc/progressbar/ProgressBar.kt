package com.example.zocdoc.progressbar

import android.app.Activity
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import com.example.zocdoc.R

class ProgressBar(private val activity : Activity) {
    private lateinit var isDialog: AlertDialog

    fun startDialog() {
        val inflate = activity.layoutInflater
        val dialogView = inflate.inflate(R.layout.progressbar, null)

        val builder = AlertDialog.Builder(activity)
        builder.setView(dialogView)
        builder.setCancelable(false)
        isDialog = builder.create()
        isDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        isDialog.show()
    }

    fun isDismiss() {
        isDialog.dismiss()
    }
}