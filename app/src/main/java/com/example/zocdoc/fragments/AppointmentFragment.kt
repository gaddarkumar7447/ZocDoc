package com.example.zocdoc.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.zocdoc.activity.DoctorPatient
import com.example.zocdoc.adapter.PatientAppointmentAdapter
import com.example.zocdoc.appointment.PatientAppointment
import com.example.zocdoc.databinding.FragmentAppointmentBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class AppointmentFragment : Fragment() {
    private lateinit var dataBinding : FragmentAppointmentBinding
    private lateinit var dbref : DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var appointmentList : ArrayList<PatientAppointment>
    private lateinit var patientAppointmentAppointment: PatientAppointmentAdapter

    @SuppressLint("SimpleDateFormat")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = FragmentAppointmentBinding.inflate(layoutInflater, container, false)

        // shared preferences
        sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)


        appointmentList = ArrayList()
        patientAppointmentAppointment = PatientAppointmentAdapter(requireContext(), appointmentList)

        dataBinding.appointmentRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        dataBinding.appointmentRecyclerview.setHasFixedSize(true)


        val isDoctor = sharedPreferences.getString("isDoctor", "Not found").toString()
        if (isDoctor == "Doctor"){
            dataBinding.toPatientList.isVisible = true
            dataBinding.toPatientList.setOnClickListener{
                startActivity(Intent(requireContext(), DoctorPatient::class.java))
            }
        }

        // select date
        dataBinding.selectDate.setOnClickListener{

            // disabled previous date
            /*val calendar = Calendar.getInstance(TimeZone.getDefault())
            calendar.timeInMillis = System.currentTimeMillis()

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setSelection(calendar.timeInMillis)
                .setMinDate(calendar.timeInMillis)
                .setTitleText("Select a date")
                .build()*/

            // Show the date picker
           // requireFragmentManager().let { datePicker.show(it, "DatePicker") }

            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            requireFragmentManager().let { datePicker.show(it, "datePicker") }
            //datePicker.show(requireFragmentManager(), "as")
            val date : StringBuilder = StringBuilder("")

            datePicker.addOnPositiveButtonClickListener {
                val dateFormate = SimpleDateFormat("dd-MM-yyyy")
                val tempDate = dateFormate.format(Date(it)).toString().trim()
                date.setLength(0)
                date.append(tempDate)
                appointmentList.clear()
                getDate(date.toString())
            }
            datePicker.addOnNegativeButtonClickListener {}
            datePicker.addOnCancelListener {  }
        }


        return dataBinding.root
    }

    private fun getDate(date: String) {
        //        val current = LocalDateTime.now()
        val userID = sharedPreferences.getString("uid","Not found").toString()
//        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
//        val date = current.format(formatter)

        dbref = FirebaseDatabase.getInstance().getReference("Users").child(userID).child("PatientsAppointments").child(date)

        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (appointmentSnapshot in snapshot.children){
                        val appointment = appointmentSnapshot.getValue(PatientAppointment::class.java)
                        appointmentList.add(appointment!!)
                    }
                    dataBinding.appointmentRecyclerview.adapter = patientAppointmentAppointment
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireActivity(),
                    error.message, Toast.LENGTH_SHORT).show()
            }
        })

        dataBinding.selectDateTextToHide.visibility = View.GONE
    }
}