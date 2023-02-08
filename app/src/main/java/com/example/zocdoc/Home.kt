package com.example.zocdoc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.zocdoc.databinding.ActivityMainBinding

class Home : AppCompatActivity() {
    private lateinit var dataBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide()

    }
}