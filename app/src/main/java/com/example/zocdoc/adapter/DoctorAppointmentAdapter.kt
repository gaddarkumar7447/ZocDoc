package com.example.zocdoc.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.zocdoc.R
import com.example.zocdoc.appointment.DoctorAppointment

class DoctorAppointmentAdapter(private val context: Context, private val doctorAppointment: ArrayList<DoctorAppointment>) : RecyclerView.Adapter<DoctorAppointmentAdapter.DoctorAppointmentViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorAppointmentViewHolder {
        return DoctorAppointmentViewHolder(LayoutInflater.from(context).inflate(R.layout.appointment_list, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DoctorAppointmentViewHolder, position: Int) {
        val currentItem = doctorAppointment[position]

        if (currentItem.PatientPhone == "" || currentItem.PatientPhone!!.isEmpty()) {
            holder.name.text = currentItem.PatientName
        } else {
            holder.name.text = currentItem.PatientName + " (" + currentItem.PatientPhone + ")"
        }

        holder.diseases.text = currentItem.Disease + " - " +currentItem.PatientCondition


        // Download Prescription
        holder.downloadPrescription.setOnClickListener{

            // Custom Chrome tab
            try {
                val builder = CustomTabsIntent.Builder()
                builder.setToolbarColor(ContextCompat.getColor(holder.downloadPrescription.context, R.color.dull_blue))
                builder.addDefaultShareMenuItem()
                builder.setShowTitle(true)
                builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)
                builder.setShareState(CustomTabsIntent.SHARE_STATE_DEFAULT)
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(holder.downloadPrescription.context, Uri.parse(currentItem.Prescription.toString().trim()))
            }catch (e : Exception){
                Toast.makeText(context, "prescription not found", Toast.LENGTH_LONG).show()
            }

            /*val intent = Intent(Intent.ACTION_VIEW, Uri.parse(currentItem.Prescription.toString().trim()))
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            holder.downloadPrescription.context.startActivity(intent)*/
        }
    }

    override fun getItemCount(): Int {
        return doctorAppointment.size
    }

    class DoctorAppointmentViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.nameDisplay1)
        val diseases: TextView = view.findViewById(R.id.diseaseDisplay1)
        val downloadPrescription : ImageView = view.findViewById(R.id.downloadPrescription)
    }
}