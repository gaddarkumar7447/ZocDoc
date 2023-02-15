package com.example.zocdoc.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.method.DialerKeyListener
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import com.example.zocdoc.R
import com.example.zocdoc.activity.AddPrescriptionActivity
import com.example.zocdoc.activity.ProfileActivity
import com.example.zocdoc.activity.UPIManager
import com.example.zocdoc.auth.LogInActivity
import com.example.zocdoc.auth.SignInActivity
import com.example.zocdoc.databinding.FragmentSettingBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SettingFragment : Fragment() {
    private lateinit var dataBinding : FragmentSettingBinding
    private lateinit var db : DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var userID : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = FragmentSettingBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        db = FirebaseDatabase.getInstance().reference

        getDataFromSharedPreference()

        dataBinding.logoutButton.setOnClickListener{
            logOut()
        }

        dataBinding.profile.setOnClickListener{
            startActivity(Intent(requireContext(), ProfileActivity::class.java))
        }

        dataBinding.createUPI.setOnClickListener{
            startActivity(Intent(requireContext(), UPIManager::class.java))
        }

        dataBinding.updatePrescription.setOnClickListener{
            startActivity(Intent(requireContext(), AddPrescriptionActivity::class.java))
            db.child("Users").child(userID).child("Prescription").addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val presURL = snapshot.child("fileurl").value.toString().trim()
                    val editor = sharedPreferences.edit()
                    editor.putString("prescription", presURL)
                    editor.apply()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

        return dataBinding.root
    }

    private fun logOut() {
        val alert = AlertDialog.Builder(requireContext())
        alert.setTitle("Alert")
        alert.setIcon(R.drawable.img_3)
        alert.setMessage("\nDo you want to logout?")
        alert.setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, LogInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            requireActivity().finish()
        })
        alert.setNegativeButton("No", DialogInterface.OnClickListener { dialogInterface, i ->

        })
        alert.setCancelable(false)
        alert.show()
    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            getDataFromSharedPreference()
        }, 1000)
    }

    private fun getDataFromSharedPreference() {
        userID = sharedPreferences.getString("uid", "Not found").toString()
    }
}