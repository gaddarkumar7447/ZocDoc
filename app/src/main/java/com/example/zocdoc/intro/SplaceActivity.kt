package com.example.zocdoc.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.zocdoc.R
import com.example.zocdoc.databinding.ActivitySplaceBinding

class SplaceActivity : AppCompatActivity() {
    private lateinit var dataBinding : ActivitySplaceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_splace)

    }
}