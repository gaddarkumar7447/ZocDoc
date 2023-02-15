package com.example.zocdoc.activity

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DoctorPatient : AppCompatActivity() {
    private lateinit var doctorAppointmentAdapter : DoctorAppointmentAdapter
    private lateinit var doctorAppointList : ArrayList<DoctorAppointment>
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var dbRef : Query

    private lateinit var dataBinding : ActivityDoctorPatientBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityDoctorPatientBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)

        sharedPreferences = baseContext.getSharedPreferences("UserData", Context.MODE_PRIVATE)
        val userID = FirebaseAuth.getInstance().currentUser?.uid

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

        doctorAppointList = ArrayList()
        doctorAppointmentAdapter = DoctorAppointmentAdapter(this, doctorAppointList)

        dataBinding.appointmentRecyclerview.layoutManager = LinearLayoutManager(this)
        dataBinding.appointmentRecyclerview.setHasFixedSize(true)

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
        dataBinding.selectDate.visibility = View.GONE
    }
}