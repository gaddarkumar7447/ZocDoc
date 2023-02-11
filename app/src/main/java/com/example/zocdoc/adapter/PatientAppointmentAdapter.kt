package com.example.zocdoc.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.zocdoc.R
import com.example.zocdoc.activity.DoctorPatient
import com.example.zocdoc.appointment.PatientAppointment

class PatientAppointmentAdapter(private val context: Context, private val appointmentList: ArrayList<PatientAppointment>) : RecyclerView.Adapter<PatientAppointmentAdapter.AppointMentViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointMentViewHolder {
        return AppointMentViewHolder(LayoutInflater.from(context).inflate(R.layout.patient_list, parent, false))
    }

    override fun onBindViewHolder(holder: AppointMentViewHolder, position: Int) {
        val currentItem = appointmentList[position]
        val name = currentItem.DoctorName
        val uid = currentItem.DoctorUID
        val disease = currentItem.Disease
        val date = currentItem.Date
        val time = currentItem.Time

        holder.name.text = name
        holder.date.text = date
        holder.disease.text = disease
        holder.time.text = time

        holder.itemView.setOnClickListener{
            val mIntent = Intent(context, DoctorPatient::class.java)
            mIntent.putExtra("uid", uid)
            mIntent.putExtra("date", date)
            mIntent.putExtra("hide", "hide")
            context.startActivity(mIntent)
        }
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }


    class AppointMentViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val name : TextView = view.findViewById(R.id.nameDisplay)
        val disease: TextView = itemView.findViewById(R.id.diseaseDisplay)
        val time:TextView = itemView.findViewById(R.id.timeDisplay)
        val date:TextView = itemView.findViewById(R.id.dateDisplay)
    }
}