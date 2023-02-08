package com.example.zocdoc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zocdoc.R
import com.example.zocdoc.databinding.FragmentAppointmentBinding

// TODO: Rename parameter arguments, choose names that match

class AppointmentFragment : Fragment() {
    private lateinit var dataBinding : FragmentAppointmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = FragmentAppointmentBinding.inflate(layoutInflater, container, false)

        return dataBinding.root
    }
}