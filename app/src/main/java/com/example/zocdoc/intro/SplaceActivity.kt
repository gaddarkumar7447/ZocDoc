package com.example.zocdoc.intro

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.databinding.DataBindingUtil
import com.example.zocdoc.Home
import com.example.zocdoc.R
import com.example.zocdoc.databinding.ActivitySplaceBinding
import com.example.zocdoc.onboarding.OnBindingActivity
import com.google.firebase.auth.FirebaseAuth

class SplaceActivity : AppCompatActivity() {
    private lateinit var dataBinding: ActivitySplaceBinding
    private lateinit var firebaseAuth: FirebaseAuth
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_splace)
        firebaseAuth = FirebaseAuth.getInstance()

        supportActionBar?.hide()

        try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            val versionName = packageInfo.versionName
            val versionCode = packageInfo.versionCode
            dataBinding.showVersion.text = "Version: $versionName.$versionCode"
        }catch (e : Exception){
            e.printStackTrace()
        }

        val currentUser = firebaseAuth.currentUser //Get the current user

        if (currentUser == null) sendUserToLoginActivity() //If the user has not logged in, send them to On-Boarding Activity
        else {
            //If user was logged in last time
            Handler().postDelayed({
                val loginIntent: Intent
                if (currentUser.isEmailVerified)
                    loginIntent = Intent(this, Home::class.java) //If the user email is verified
                else
                    loginIntent = Intent(this, OnBindingActivity::class.java) //If the user email is not verified
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(loginIntent)
                finish()
            }, 2000)
        }
    }

    private fun sendUserToLoginActivity() {
        Handler().postDelayed({
            val loginIntent = Intent(this, OnBindingActivity::class.java)
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(loginIntent)
            finish()
        }, 2000)
    }
}