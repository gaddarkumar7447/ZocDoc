package com.example.zocdoc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zocdoc.R
import com.example.zocdoc.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {
    private lateinit var dataBinding : FragmentSettingBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = FragmentSettingBinding.inflate(inflater, container, false)

        return dataBinding.root
    }
}