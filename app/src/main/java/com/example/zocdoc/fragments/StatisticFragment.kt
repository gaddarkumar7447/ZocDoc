package com.example.zocdoc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zocdoc.databinding.FragmentStatisticBinding

class StatisticFragment : Fragment() {
    private lateinit var dataBinding: FragmentStatisticBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = FragmentStatisticBinding.inflate(inflater, container, false)

        return dataBinding.root
    }

}