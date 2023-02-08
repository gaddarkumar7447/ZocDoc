package com.example.zocdoc.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.zocdoc.R
import com.example.zocdoc.databinding.ActivityForgetPasswordBinding

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var dataBinding : ActivityForgetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password)

    }
}