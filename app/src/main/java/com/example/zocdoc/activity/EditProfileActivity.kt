package com.example.zocdoc.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.zocdoc.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import io.grpc.okhttp.internal.Util

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.confirm.setOnClickListener{
            val name = binding.editName.text.toString().trim()
            val age = binding.editAge.text.toString().trim()
            val phone = binding.editPhoneNumber.text.toString().trim()

            if (name.isNotEmpty() && age.isNotEmpty() && phone.isNotEmpty()){
                updateDetails(name, age, com.example.zocdoc.Util().removeCountryCode(phone))
            }
            else{
                Toast.makeText(this, "Details are required", Toast.LENGTH_LONG).show()
            }
        }

        binding.updatepres.setOnClickListener{
            startActivity(Intent(this, AddPrescriptionActivity::class.java))
        }
    }

    private fun updateDetails(name: String, age: String, phone: String) {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val update = FirebaseDatabase.getInstance().getReference("Users").child(currentUid)
        /*update.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    update.child("name").setValue(name).toString()
                    update.child("age").setValue(age).toString()
                    update.child("phone").setValue(phone).toString()
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })*/

        val userChange = mapOf<String, String>("name" to name, "age" to age, "phone" to phone)

        update.updateChildren(userChange).addOnSuccessListener {
            binding.editAge.text.clear()
            binding.editPhoneNumber.text.clear()
            binding.editName.text.clear()
            Toast.makeText(this, "Update successful", Toast.LENGTH_LONG).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Failed to update", Toast.LENGTH_LONG).show()
        }
    }
}