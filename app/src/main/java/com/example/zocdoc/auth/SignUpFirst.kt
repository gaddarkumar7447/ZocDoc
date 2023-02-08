package com.example.zocdoc.auth

import android.content.DialogInterface
import android.content.Intent
import android.graphics.text.LineBreaker.ParagraphConstraints
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.example.zocdoc.R
import com.example.zocdoc.Util
import com.example.zocdoc.databinding.ActivityForgetPasswordBinding.inflate
import com.example.zocdoc.databinding.ActivitySignUpFirstBinding
import com.example.zocdoc.databinding.ParagraphModelBinding

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

        dataBinding.infoOfDoctor.setOnClickListener{
            /*val dialog = ParagraphModelBinding.inflate(layoutInflater)
            val bottomSheet = Util().createBottomSheet(this)
            dialog.apply {
                paragraphHeading.text = "Gaddar"
                paragraphContent.text = "Gagagag"
            }
            val paragraphModelBinding = ParagraphModelBinding.bind(View(this))
            paragraphModelBinding.root*/
            val alert = AlertDialog.Builder(this)
            alert.setTitle("Notices")
            alert.setIcon(R.drawable.img)
            alert.setMessage("1. If you are doctor than swipe the button.\n2. If you are patient then no need for swipe")
            alert.setPositiveButton("Got it", DialogInterface.OnClickListener { dialogInterface, i -> })
            alert.setCancelable(false)
            alert.show()
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
                Toast.makeText(this, "Please enter the age", Toast.LENGTH_SHORT).show()
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