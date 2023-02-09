package com.example.zocdoc.auth

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.util.Patterns
import android.view.MotionEvent
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.zocdoc.R
import com.example.zocdoc.Util
import com.example.zocdoc.databinding.ActivitySignInBinding
import com.example.zocdoc.progressbar.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignInActivity : AppCompatActivity() {
    private lateinit var dataBinding : ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db : DatabaseReference
    private lateinit var fd : FirebaseDatabase
    private lateinit var progressBar : ProgressBar

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        supportActionBar?.hide()

        progressBar = ProgressBar(this)

        val isDoctor = intent.extras?.getString("isDoctor")
        val age = intent.extras?.getString("age")

        if (isDoctor == "Doctor"){
            dataBinding.menuDoctorType.visibility = View.VISIBLE
        }

        var passwordVisible = false
        dataBinding.SignUpPassword.setOnTouchListener { v, event ->
            val Right = 2
            if (event.getAction() === MotionEvent.ACTION_UP) {
                if (event.rawX >= dataBinding.SignUpPassword.right - dataBinding.SignUpPassword.compoundDrawables[Right].bounds.width()) {
                    val selection: Int = dataBinding.SignUpPassword.selectionEnd
                    //Handles Multiple option popups
                    if (passwordVisible) {
                        //set drawable image here
                        dataBinding.SignUpPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.visibility_off,
                            0
                        )
                        //for hide password
                        dataBinding.SignUpPassword.transformationMethod =
                            PasswordTransformationMethod.getInstance()
                        passwordVisible = false
                    } else {
                        //set drawable image here
                        dataBinding.SignUpPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                            0,
                            0,
                            R.drawable.visibility,
                            0
                        )
                        //for show password
                        dataBinding.SignUpPassword.transformationMethod =
                            HideReturnsTransformationMethod.getInstance()
                        passwordVisible = true
                    }
                    dataBinding.SignUpPassword.isLongClickable = false //Handles Multiple option popups
                    dataBinding.SignUpPassword.setSelection(selection)
                    return@setOnTouchListener true
                }
            }
            false
        }

        // Disease List
        val items = listOf("Cardiologist", "Dentist", "ENT specialist", "Obstetrician/Gynaecologist", "Orthopaedic surgeon","Psychiatrists", "Radiologist", "Pulmonologist", "Neurologist", "Allergists", "Gastroenterologists", "Urologists", "Otolaryngologists", "Rheumatologists", "Anesthesiologists")
        val adapter = ArrayAdapter(this, R.layout.item_text, items)
        dataBinding.SignUpTypeOfDoctor.setAdapter(adapter)


        dataBinding.createAccount.setOnClickListener{
            val name = dataBinding.SignUpName.text.toString().trim()
            val email = dataBinding.SignUpEmail.text.toString().trim()
            val tempPhone = dataBinding.SignUpPhone.text.toString().trim()
            val password = dataBinding.SignUpPassword.text.toString().trim()

            val specialist = dataBinding.SignUpTypeOfDoctor.text.toString().trim()
            val phone = Util().removeCountryCode(tempPhone)

            progressBar.startDialog()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                if (password.length > 7) {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                        if (it.isSuccessful) {

                            val u = firebaseAuth.currentUser
                            val uid = firebaseAuth.currentUser?.uid.toString()
                            //Create user object
                            val statsData = "0:0:0:0:0?0:0:0:0:0?0:0:0:0:0?0:0:0:0:0"
                            val user = User(name, email, phone, uid, isDoctor, age, specialist, statsData, "false")

                            //add user data in the Realtime Database
                            db.child(u?.uid!!).setValue(user).addOnCompleteListener { it1 ->
                                if(it1.isSuccessful){
                                    u.sendEmailVerification()
                                    Toast.makeText(this, "Email Verification sent to your mail", Toast.LENGTH_LONG).show()
                                    startActivity(Intent(this, LogInActivity::class.java))

                                    progressBar.isDismiss()

                                    if (isDoctor == "Doctor") {
                                        fd.getReference(isDoctor).child(u.uid).setValue(user).addOnSuccessListener {}
                                    }

                                } else
                                    Log.e("Not successful", "Unsuccessful")
                                    progressBar.isDismiss()
                            }
                        } else {
                            Toast.makeText(this, "Please enter valid details", Toast.LENGTH_SHORT).show()
                            progressBar.isDismiss()
                        }
                    }
                } else {
                    Toast.makeText(this, "password is greater than 8", Toast.LENGTH_SHORT).show()
                    progressBar.isDismiss()
                }
            } else {
                Toast.makeText(this, "Please enter the details!", Toast.LENGTH_SHORT).show()
                progressBar.isDismiss()
            }
        }

        setUp()
    }

    private fun setUp() {
        firebaseAuth = FirebaseAuth.getInstance()
        fd = FirebaseDatabase.getInstance()
        db = fd.getReference("Users")

        dataBinding.toSignIn.setOnClickListener{
            startActivity(Intent(this, LogInActivity::class.java))
        }
    }


}