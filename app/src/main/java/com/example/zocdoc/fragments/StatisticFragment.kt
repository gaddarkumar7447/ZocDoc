package com.example.zocdoc.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.zocdoc.databinding.FragmentStatisticBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.robinhood.spark.SparkAdapter
import java.util.*

class StatisticFragment : Fragment() {
    private lateinit var dataBinding: FragmentStatisticBinding
    @SuppressLint("ResourceType")
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var bloodPressure : MutableList<Int>

    //Current User's data
    private lateinit var userID: String
    private lateinit var stats: String
    private lateinit var db: DatabaseReference
    val array = arrayListOf<Int>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = FragmentStatisticBinding.inflate(inflater, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)
        db = FirebaseDatabase.getInstance().reference
        getDataFromSharedPreference()
        /*val adapter = object : SparkAdapter(){
            override fun getCount(): Int {return data.size}
            override fun getItem(index: Int): Any {return data[index]}
            override fun getY(index: Int): Float {return data[index]}
        }*/
        /*dataBinding.sparkview.adapter = adapter
        dataBinding.b.setOnClickListener {
            val number = dataBinding.numberPut.text.toString().trim()
            if (number.isNotEmpty()){
                data.add(number.toFloat())
                dataBinding.numberPut.text.clear()
                dataBinding.sparkview.adapter = adapter
            }
            else{Toast.makeText(requireContext(), "Enter the number", Toast.LENGTH_SHORT).show()}
        }*/

        bloodPressure = mutableListOf<Int>(0)
        //addData()

        val adapter = object : SparkAdapter(){
            override fun getCount(): Int { return bloodPressure.size }
            override fun getItem(index: Int): Any { return bloodPressure[index] }
            override fun getY(index: Int): Float { return bloodPressure[index].toFloat() }
        }

        dataBinding.addBloodPressureData.setOnClickListener {
            val alertDialog : AlertDialog.Builder = AlertDialog.Builder(requireActivity())
            alertDialog.setTitle("Enter data Blood Pressure")
            val input = EditText(requireContext())
            input.hint = "Enter the data"
            input.inputType = InputType.TYPE_CLASS_NUMBER
            alertDialog.setView(input)

            alertDialog.setPositiveButton("Set", DialogInterface.OnClickListener { dialogInterface, i ->
                val value = input.text.toString().trim()
                if (value.isNotEmpty()){
                    input.text.clear()
                    bloodPressure.add(value.toInt())
                    dataBinding.bloodPressure.adapter = adapter
                    val addValue = db.child("Users").child(userID).child("stats")
                    addValue.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                /*val statsList = snapshot.value as MutableList<Int>
                                statsList.add(value.toInt())
                                addValue.setValue(statsList)*/
                                //bloodPressure.add(snapshot.value.toString().toInt())
                                addValue.setValue(bloodPressure)
                                //addValue.setValue(bloodPressure.add(value.toInt()))
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })

                    val editor = sharedPreferences.edit()
                    editor.putString("stats", bloodPressure.toString())
                    editor.apply()

                   /* val fragmentId = findNavController().currentDestination?.id
                    findNavController().popBackStack(fragmentId!!, true)
                    findNavController().navigate(fragmentId)*/
                }else{
                    input.error = "Enter the data"
                }
            })
            alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ -> dialog.cancel()})
            alertDialog.show()
        }

        return dataBinding.root
    }

    private fun addData() {
        val list1 = mutableListOf<Int>()
        db.child("Users").child(userID).child("stats").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val data = snapshot.value as List<*>
                    for (i in data){
                        list1.add(i.toString().toInt())
                    }
                    Log.d("Data", "Data: $list1")
                    /*val valueOfStats = snapshot.value.toString()
                    val regex = Regex("[^\\d,-]")
                    val cleanedValues = regex.replace(valueOfStats, "")
                    val valuesArray = cleanedValues.split(",").toTypedArray()
                    list1.addAll(valuesArray.map { it.toInt() })
                    Log.d("BloodData", "i value: $list1")
                    for (value in list1){
                        array.add(value)
                    }*/
                    val adapter = object : SparkAdapter(){
                        override fun getCount(): Int { return list1.size }
                        override fun getItem(index: Int): Any { return list1[index] }
                        override fun getY(index: Int): Float { return list1[index].toFloat() }
                    }
                    dataBinding.bloodPressure.adapter = adapter
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }


    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            getDataFromSharedPreference()
        }, 1000)
    }

    private fun getDataFromSharedPreference() {
        userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
        stats = sharedPreferences.getString("stats", "0:0:6:0:0?0:0:0:0:0?0:0:0:0:0?0:0:0:0:0").toString()
    }

}