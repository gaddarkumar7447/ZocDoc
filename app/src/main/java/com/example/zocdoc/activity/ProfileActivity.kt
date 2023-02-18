package com.example.zocdoc.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.zocdoc.R
import com.example.zocdoc.databinding.ActivityProfileAcitivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfileAcitivityBinding
    private lateinit var sharedPreferences: SharedPreferences
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileAcitivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = this.getSharedPreferences("UserData", Context.MODE_PRIVATE)

        /*val name = sharedPreferences.getString("name", "").toString()
        val age = sharedPreferences.getString("age", "").toString()
        val email = sharedPreferences.getString("email", "").toString()
        val phone = sharedPreferences.getString("phone", "").toString()

        binding.name.text = "Name: $name"
        binding.age.text = "Age: $age"
        binding.phone.text = "Phone: $phone"
        binding.email.text = "Email: $email"*/

        binding.email.text = "Email: ${FirebaseAuth.getInstance().currentUser?.email}"

        val isDoctor = sharedPreferences.getString("isDoctor", "Not found").toString()

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        FirebaseDatabase.getInstance().reference.child("Users").child(currentUserId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val name = snapshot.child("name").value.toString()
                    val age = snapshot.child("age").value.toString()
                    val phone = snapshot.child("phone").value.toString()

                    binding.name.text = "Name: $name"
                    binding.age.text = "Age: $age"
                    binding.phone.text = "Phone: $phone"

                    if (isDoctor == "Doctor"){
                        binding.name.text = "Name: Dr. $name"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}

        })
        binding.ProfileToEdit.setOnClickListener{
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

    }
}