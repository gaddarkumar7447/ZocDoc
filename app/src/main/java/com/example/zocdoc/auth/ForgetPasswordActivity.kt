package com.example.zocdoc.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.zocdoc.R
import com.example.zocdoc.databinding.ActivityForgetPasswordBinding
import com.example.zocdoc.progressbar.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import java.util.regex.Pattern

class ForgetPasswordActivity : AppCompatActivity() {
    private lateinit var dataBinding : ActivityForgetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_forget_password)

        val progressBar = ProgressBar(this)
        supportActionBar?.hide()

        val firebaseAuth = FirebaseAuth.getInstance()
        dataBinding.ForgotPassButton.setOnClickListener{
            val email = dataBinding.ForgotPassEmail.text.toString().trim()
            progressBar.startDialog()
            if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener{
                    if (it.isSuccessful){
                        Toast.makeText(this, "Email has been send", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, LogInActivity::class.java))
                        progressBar.isDismiss()
                    }
                    else{
                        Toast.makeText(this, "Please sign in", Toast.LENGTH_LONG).show()
                        progressBar.isDismiss()
                    }
                }
            }else{
                Toast.makeText(this,"Please enter the valid email", Toast.LENGTH_LONG).show()
                progressBar.isDismiss()
            }
        }
    }

}