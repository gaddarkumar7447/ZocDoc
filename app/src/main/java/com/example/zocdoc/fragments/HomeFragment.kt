package com.example.zocdoc.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Vibrator
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import com.example.zocdoc.Util
import com.example.zocdoc.appointment.AppointmentBooking
import com.example.zocdoc.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.ncorti.slidetoact.SlideToActView

class HomeFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db : DatabaseReference
    // current user data
    private lateinit var userName : String
    private lateinit var userEmail : String
    private lateinit var userPhone : String
    private lateinit var userId : String
    private lateinit var userPosition : String
    private lateinit var userType : String
    private var userPrescription : String = "false"

    // doctor data
    private lateinit var searchedName : String
    private lateinit var searchedEmail : String
    private lateinit var searchedPhone : String
    private lateinit var searchedData : String
    private lateinit var searchedUid : String
    private lateinit var searchedType : String

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var dataBindingFragment : FragmentHomeBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBindingFragment = FragmentHomeBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().reference

        sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        getPreferences()

        dataBindingFragment.doctorData.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_DONE){
                searchedData = dataBindingFragment.doctorData.text.toString().trim()
                if (searchedData.isNotEmpty()){
                    if (Util().removeCountryCode(searchedData) == userPhone || searchedData == userPhone || searchedData == userEmail || isSameName(searchedData, userName)){
                        Toast.makeText(requireContext(), "Stop searching yourself", Toast.LENGTH_SHORT).show()
                        dataBindingFragment.cardView.isVisible = false
                        dataBindingFragment.slider.isVisible = false
                    }else{
                        try{
                            doctorIsPresent()
                        }catch (e : Exception){
                            Toast.makeText(requireContext(), "$e", Toast.LENGTH_LONG).show()
                        }
                    }
                }else{
                    Toast.makeText(requireContext(), "Enter doctor's Email/Phone", Toast.LENGTH_SHORT).show()
                }
            }
            false
        }


        dataBindingFragment.slider.animDuration = 150
        dataBindingFragment.slider.onSlideCompleteListener = object : SlideToActView.OnSlideCompleteListener{
            override fun onSlideComplete(view: SlideToActView) {
                if (userPrescription != "false"){
                    val intent = Intent(requireContext(), AppointmentBooking::class.java)
                    intent.putExtra("Dname", searchedName)
                    intent.putExtra("Demail", searchedEmail)
                    intent.putExtra("Dphone", searchedPhone)
                    intent.putExtra("Dtype", searchedType)
                    intent.putExtra("Duid", searchedUid)
                    try {
                        startActivity(intent)
                    }catch (e : Exception){
                        Toast.makeText(requireActivity(), "$e", Toast.LENGTH_LONG).show()
                        //Log.d("Some", "Some error: $e")
                    }
                    // Adding vibrate
                    view.bumpVibration = 100
                    val vibrator = requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                    if (vibrator.hasVibrator()) {
                        vibrator.vibrate(100)
                    }
                    dataBindingFragment.slider.resetSlider()
                }else{
                    Toast.makeText(requireActivity(), "Please upload your prescription in settings tab", Toast.LENGTH_SHORT).show()
                    dataBindingFragment.slider.resetSlider()
                }
            }

        }

        return dataBindingFragment.root
    }

    private fun doctorIsPresent() {
        db.child("Doctor").addValueEventListener(object : ValueEventListener{
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot : DataSnapshot) {
                for (data in snapshot.children){
                   val map = data.value as Map<*, *>
                    val sName = map["name"].toString().trim()
                    // Log.d("Sname", "name: $sName")
                    val sPhone = map["phone"].toString().trim()
                    val sType = map["specialist"].toString().trim()
                    val sEmail = map["email"].toString().trim()
                    val sUid = map["uid"].toString().trim()
                    if (searchedData.trim() == sName || searchedData == sPhone || searchedData == sEmail || isSameName(searchedData, sName) || Util().removeCountryCode(searchedData) == sPhone){
                        searchedName = sName
                        searchedEmail = sEmail
                        searchedPhone = sPhone
                        searchedType = sType
                        searchedUid = sUid

                        dataBindingFragment.textView3.isVisible = false
                        dataBindingFragment.cardView.isVisible = true
                        dataBindingFragment.slider.isVisible = true

                        dataBindingFragment.doctorName.text = "Name : Dr. $sName"
                        dataBindingFragment.doctorPhone.text = "Phone : $sPhone"
                        dataBindingFragment.doctorEmail.text = "Email : $sEmail"
                        dataBindingFragment.doctortype.text = "Specialization : $sType"
                        return
                    }else{
                        dataBindingFragment.textView3.isVisible = true
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun isSameName(searchedData: String, userName: String): Boolean {
        val modSearched : String = searchedData.replace(" ", "").trim()
        val mod : String = userName.replace(" ", "").trim()
        return modSearched == mod
    }

    @SuppressLint("SetTextI18n")
    private fun getPreferences() {
        userId = sharedPreferences.getString("uid", "Not Found").toString()
        userName = sharedPreferences.getString("name", "Not found").toString()
        userEmail = sharedPreferences.getString("email", "Not found").toString()
        userPhone = sharedPreferences.getString("phone", "Not found").toString()
        userPosition = sharedPreferences.getString("isDoctor", "Not found").toString()
        userPrescription = sharedPreferences.getString("prescription", "false").toString()

        if (userPosition == "Doctor"){
            dataBindingFragment.namePreview.text = "Dr. $userName"
        }else{
            dataBindingFragment.namePreview.text = userName
        }
    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            getPreferences()
        }, 1000)
    }

}