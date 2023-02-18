package com.example.zocdoc.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zocdoc.adapter.DoctorAppointmentAdapter
import com.example.zocdoc.appointment.DoctorAppointment
import com.example.zocdoc.databinding.ActivityDoctorPatientBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DoctorPatient : AppCompatActivity() {
    private lateinit var doctorAppointmentAdapter : DoctorAppointmentAdapter
    private lateinit var doctorAppointList : ArrayList<DoctorAppointment>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbRef : Query

    private lateinit var dataBinding : ActivityDoctorPatientBinding
    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityDoctorPatientBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)

        sharedPreferences = baseContext.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        //val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()

        //Log.d("TAGUSER", "user: $userID")

        doctorAppointList = ArrayList()
        doctorAppointmentAdapter = DoctorAppointmentAdapter(this, doctorAppointList)
        dataBinding.appointmentRecyclerview.layoutManager = LinearLayoutManager(this)
        dataBinding.appointmentRecyclerview.setHasFixedSize(true)

        Log.d("dd", intent.getStringExtra("date").toString())
        val userID = sharedPreferences.getString("uid","Not found").toString()
        Log.d("userID", "user: $userID")
        val date : StringBuilder = StringBuilder("")
        date.append(intent.getStringExtra("date").toString())


        if (date.toString().isNotEmpty() || date.length > 0){
            val hide = intent.getStringExtra("hide")
            if (hide == "hide"){
                dataBinding.selectDate.visibility = View.VISIBLE
            }
            val doctorIntentUid = intent.getStringExtra("uid").toString()
            getDate(date.toString(), doctorIntentUid)

            Log.d("Doctor", "UID : $doctorIntentUid")

        }
        dataBinding.selectDate.visibility = View.VISIBLE

        dataBinding.selectDate.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.show(supportFragmentManager, "DatePicker")
            datePicker.addOnPositiveButtonClickListener {
                val dateFormatter = SimpleDateFormat("dd-MM-yyyy")
                val tempDate = dateFormatter.format(Date(it)).toString().trim()
                date.setLength(0)
                date.append(tempDate)
                doctorAppointList.clear()
                //dataBinding.selectDate.text = date
                getDate(date.toString(), userID)

            }
            datePicker.addOnNegativeButtonClickListener {
                Toast.makeText(this, "${datePicker.headerText} is cancelled", Toast.LENGTH_LONG).show()
            }

            datePicker.addOnCancelListener {
                Toast.makeText(this, "Date Picker Cancelled", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getDate(date: String, doctorIntentUid: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Doctor").child(doctorIntentUid).child("DoctorsAppointments").child(date).orderByChild("TotalPoints")

        dbRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (appointmentSnapShot in snapshot.children){
                        val appointment = appointmentSnapShot.getValue(DoctorAppointment::class.java)
                        doctorAppointList.add(appointment!!)
                        doctorAppointList.sortWith(compareBy{appointment.TotalPoints})
                        doctorAppointList.reverse()
                    }
                    dataBinding.appointmentRecyclerview.adapter = doctorAppointmentAdapter
                    dataBinding.selectDateTextToHide.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(baseContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}