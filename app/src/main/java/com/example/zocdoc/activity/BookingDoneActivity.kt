package com.example.zocdoc.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.zocdoc.Home
import com.example.zocdoc.databinding.ActivityBookingDoneBinding

class BookingDoneActivity : AppCompatActivity() {
    lateinit var dataBinding : ActivityBookingDoneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityBookingDoneBinding.inflate(layoutInflater)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, Home::class.java)
            startActivity(intent)
            finish()
        }, 1800)

        setContentView(dataBinding.root)
    }
}