package com.example.zocdoc.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.example.zocdoc.R
import com.example.zocdoc.databinding.ActivityUpimanagerBinding
import com.example.zocdoc.encryptionHelper.Encryption
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel
import java.util.*


class UPIManager : AppCompatActivity() {
    private lateinit var binding : ActivityUpimanagerBinding
    private lateinit var upiString : StringBuilder
    private val upiIdPattern = "[\\\\w.]+@[\\\\w]+".toRegex()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpimanagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Text watcher
        binding.upiNamePn.addTextChangedListener(textWatcher)
        binding.upiIDPa.addTextChangedListener(textWatcher)
        binding.upiAmountAm.addTextChangedListener(textWatcher)
        binding.upiMessageTn.addTextChangedListener(textWatcher)

    }
    // Text watcher
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            checkRequiredFields()
        }
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    }

    private fun checkRequiredFields() {
        val name = binding.upiNamePn.text.toString().trim()
        val upiId = binding.upiIDPa.text.toString().trim()
        val amount = binding.upiAmountAm.text.toString().trim()
        val message = binding.upiMessageTn.text.toString().trim()

        if (name.isNotEmpty() && upiId.isNotEmpty() && amount.isNotEmpty() && message.isNotEmpty()) {
            binding.generateUPI.visibility = View.VISIBLE
            binding.generateUPI.setOnClickListener {
                generateQrCode(name, upiId, amount, message)
            }
        } else {
            binding.generateUPI.visibility = View.GONE
            binding.qrPreview.visibility = View.GONE
            binding.link.visibility = View.GONE
        }
    }

    private fun generateQrCode(name : String, upiId : String, amount : String, message : String) {
        binding.qrPreview.visibility = View.VISIBLE
        binding.link.visibility = View.VISIBLE
        upiString = StringBuilder()

        //upiString.append("upi://pay?pa=").append(upiId).append("&pn=").append(name).append("&am=").append(amount).append("&cu=INR&")

        /*if (message.isNotEmpty() && message.isNotBlank()) {
            upiString.append(message).append("&")
        }*/

        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            /*.appendQueryParameter("tr", "<transaction ID>")*/
            .appendQueryParameter("tn", message)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .build()

        binding.qrPreview.setImageBitmap(generateQrCodeImage(uri.toString()))


        binding.link.text = uri.toString()

        binding.link.setOnClickListener {
            gotoPaymentMethod(uri)
        }

        binding.qrPreview.visibility = View.VISIBLE
        binding.link.visibility = View.VISIBLE
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun gotoPaymentMethod(uri : Uri) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = uri

            // Create a chooser dialog for payment apps
            val chooser = Intent.createChooser(intent, "Pay with")
            val pm = packageManager
            val activities = pm.queryIntentActivities(intent, 0)
            for (app in activities) {
                if (app.activityInfo.packageName.contains("phonepe", ignoreCase = true)) {
                    chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(Intent(intent).setPackage(app.activityInfo.packageName)))
                    break
                }
            }
            startActivity(chooser)
            /*intent.setPackage("net.one97.paytm")
            startActivity(intent)*/
        } catch (e: Exception) {
            Toast.makeText(this, "Paytm not installed on your device", Toast.LENGTH_SHORT).show()
        }
    }

    private fun generateQrCodeImage(toString: String): Bitmap? {
        val hintMap = Hashtable<EncodeHintType, ErrorCorrectionLevel>()
        hintMap[EncodeHintType.ERROR_CORRECTION] = ErrorCorrectionLevel.H

        val qrCodeWriter = QRCodeWriter()
        val size = 512
        val bitMatrix = qrCodeWriter.encode(toString, BarcodeFormat.QR_CODE, size, size)
        val width = bitMatrix.width
        val bmp = Bitmap.createBitmap(width, width, Bitmap.Config.RGB_565)
        for (x in 0 until width)
            for (y in 0 until width)
                bmp.setPixel(y, x, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)

        return bmp
    }
}