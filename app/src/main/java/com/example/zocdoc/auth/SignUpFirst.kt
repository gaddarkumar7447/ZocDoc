package com.example.zocdoc.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.zocdoc.R
import com.example.zocdoc.databinding.ActivitySignUpFirstBinding
import com.github.angads25.toggle.interfaces.OnToggledListener
import com.github.angads25.toggle.model.ToggleableView

class SignUpFirst : AppCompatActivity() {
    private lateinit var dataBinding : ActivitySignUpFirstBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_first)

        supportActionBar?.hide()

        var  isDoctor : String = ""
        dataBinding.stickySwitch.setOnToggledListener { toggleableView, isOn ->
            if (isOn){
                isDoctor = "Doctor"
                Toast.makeText(this, "You are doctor", Toast.LENGTH_LONG).show()
            }else{
                isDoctor = ""
                Toast.makeText(this, "You are patient", Toast.LENGTH_SHORT).show()
            }

            Log.d("doc", "Doctor : $isDoctor")
        }

        dataBinding.nextButton.setOnClickListener {
            val age = dataBinding.ageInput.text.toString().trim()
            //val isDoctor = binding.stickySwitch.getText()

            if (isDoctor == "Doctor") {
                if (age.isEmpty()){
                    Toast.makeText(this, "Enter the age", Toast.LENGTH_SHORT).show()
                }else if (Integer.parseInt(age) < 23){
                    Toast.makeText(this, "Age should be greater than 23", Toast.LENGTH_LONG).show()
                }
                else{
                    val intent = Intent(this, SignInActivity::class.java)
                    intent.putExtra("isDoctor", isDoctor)
                    intent.putExtra("age", age)
                    startActivity(intent)
                }
            }else if (isDoctor == "" && age.isEmpty()) {
                Toast.makeText(this, "Enter age", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(this, SignInActivity::class.java)
                intent.putExtra("isDoctor", isDoctor)
                intent.putExtra("age", age)
                startActivity(intent)
            }
        }

    }
}


/*
* binding.nextButton.setOnClickListener {
            val age = binding.ageInput.text.toString().trim()
            val isDoctor = binding.stickySwitch.getText()

            if (isDoctor == "Doctor" && (Integer.parseInt(age) < 23)) {
                Toast.makeText(baseContext, "23 is the minimum age of a Doctor", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }else if (age.isEmpty()) {
                Toast.makeText(baseContext, "Enter age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val intent = Intent(this, SignUp_Activity::class.java)
                intent.putExtra("isDoctor", isDoctor)
                intent.putExtra("age", age)
                startActivity(intent)
            }

        }
        * */