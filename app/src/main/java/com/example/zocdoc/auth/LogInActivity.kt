package com.example.zocdoc.auth

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.zocdoc.MainActivity
import com.example.zocdoc.R
import com.example.zocdoc.databinding.ActivityLogInBinding
import com.example.zocdoc.encryptionHelper.Encryption
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LogInActivity : AppCompatActivity() {
    private lateinit var dataBinding : ActivityLogInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    @SuppressLint("ClickableViewAccessibility", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_log_in)

        supportActionBar?.hide()
        firebaseAuth = FirebaseAuth.getInstance()

        methodCall()

    }

    private fun methodCall() {
        initilisize()
        logIn()
        passwordVisible()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun passwordVisible() {
        var passwordVisible = false
        dataBinding.SignInPassword.setOnTouchListener { v, event ->
            val Right = 2
            if (event.getAction() === MotionEvent.ACTION_UP) {
                if (event.rawX >= dataBinding.SignInPassword.right - dataBinding.SignInPassword.compoundDrawables[Right].bounds.width()) {
                    val selection: Int = dataBinding.SignInPassword.selectionEnd
                    //Handles Multiple option popups
                    if (passwordVisible) {
                        //set drawable image here
                        dataBinding.SignInPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility_off, 0)
                        //for hide password
                        dataBinding.SignInPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                        passwordVisible = false
                    } else {
                        //set drawable image here
                        dataBinding.SignInPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.visibility, 0)
                        //for show password
                        dataBinding.SignInPassword.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                        passwordVisible = true
                    }
                    dataBinding.SignInPassword.isLongClickable = false //Handles Multiple option popups
                    dataBinding.SignInPassword.setSelection(selection)
                    return@setOnTouchListener true
                }
            }
            false
        }
    }

    private fun logIn() {
        val sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        dataBinding.loginButton.setOnClickListener(View.OnClickListener {
            val email = dataBinding.SignInEmail.text.toString().trim()
            val password = dataBinding.SignInPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()){
                if (password.length > 7){
                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener{
                        if (it.isSuccessful){
                            val u = firebaseAuth.currentUser
                            if (u?.isEmailVerified!!){
                                val db = FirebaseDatabase.getInstance().reference
                                val encryption = Encryption.getDefault("Key", "Salt", ByteArray(16))
                                db.child("Users").child(u.uid).addValueEventListener(object : ValueEventListener{
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        editor.putString("uid", snapshot.child("uid").value.toString().trim())
                                        editor.putString("name", snapshot.child("name").value.toString().trim())
                                        editor.putString("age", snapshot.child("age").value.toString().trim())
                                        editor.putString("email", snapshot.child("email").value.toString().trim())
                                        editor.putString("phone", snapshot.child("phone").value.toString().trim())
                                        editor.putString("isDoctor", snapshot.child("doctor").value.toString().trim())
                                        editor.putString("specialist",snapshot.child("specialist").value.toString().trim())
                                        editor.putString("stats", snapshot.child("stats").value.toString().trim())
                                        editor.putString("prescription", snapshot.child("prescription").value.toString().trim())
                                        editor.putString("upi", snapshot.child(encryption.encrypt("nulla")).value.toString().trim())
                                        editor.apply()
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                        Log.d("Tag", "Somethings wrong")
                                    }
                                })
                                startActivity(Intent(this, MainActivity::class.java))
                            }else {
                                u.sendEmailVerification()
                                Toast.makeText(this, "Email Verification sent to your mail", Toast.LENGTH_LONG).show()
                            }
                        }else{
                            Toast.makeText(this, "Somethings wrong", Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    Toast.makeText(this, "Password length must be greater than 8", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Please enter the details", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initilisize() {
        dataBinding.forgotPassword.setOnClickListener{
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }
        dataBinding.toSignUp.setOnClickListener{
            startActivity(Intent(this, SignUpFirst::class.java))
        }
    }
}