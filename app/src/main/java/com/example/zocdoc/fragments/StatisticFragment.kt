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
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.zocdoc.databinding.FragmentStatisticBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.robinhood.spark.SparkAdapter
import java.util.*
import java.util.Collections.max
import java.util.Collections.min

class StatisticFragment : Fragment() {
    private lateinit var dataBinding: FragmentStatisticBinding
    @SuppressLint("ResourceType")
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var bloodPressure : MutableList<Int>

    //Current User's data
    private lateinit var userID: String
    private lateinit var stats: String
    private lateinit var db: DatabaseReference

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = FragmentStatisticBinding.inflate(inflater, container, false)
        sharedPreferences = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)

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

        //bloodPressure = mutableListOf<Int>(0)
        //addData()

        /*val adapter = object : SparkAdapter(){
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
                                *//*val statsList = snapshot.value as MutableList<Int>
                                statsList.add(value.toInt())
                                addValue.setValue(statsList)*//*
                                //bloodPressure.add(snapshot.value.toString().toInt())

                                //addValue.setValue(bloodPressure)

                                //addValue.setValue(bloodPressure.add(value.toInt()))
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {}
                    })

                    val editor = sharedPreferences.edit()
                    editor.putString("stats", bloodPressure.toString())
                    editor.apply()

                   *//* val fragmentId = findNavController().currentDestination?.id
                    findNavController().popBackStack(fragmentId!!, true)
                    findNavController().navigate(fragmentId)*//*
                }else{
                    input.error = "Enter the data"
                }
            })
            alertDialog.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, _ -> dialog.cancel()})
            alertDialog.show()
        }*/

        db = FirebaseDatabase.getInstance().reference
        getDataFromSharedPreference()
        val splitParts = stats.split("?")

        setBloodPressure(splitParts[0], splitParts)
        setSugarFasting(splitParts[1], splitParts)
        setSugarPP(splitParts[2], splitParts)
        setCholesterol(splitParts[3], splitParts)


        return dataBinding.root
    }

    @SuppressLint("SetTextI18n")
    private fun setBloodPressure(Concat : String, splitParts: List<String>) {
        val splitBloodPressure = Concat.split(":")
        val bloodPressureList: ArrayList<Int> = ArrayList()
        for (i in 0..4) {
            bloodPressureList.add(Integer.parseInt(splitBloodPressure[i]))
        }
        val bloodPressureMin = min(bloodPressureList)
        val bloodPressureMax = max(bloodPressureList)
        /*if (bloodPressureMax == bloodPressureMax) {
            dataBinding.bloodPressureRange.text = "Value is constant!"
            dataBinding.bloodPressureRange.setTextColor(ContextCompat.getColor(requireActivity(), R.color.common_google_signin_btn_text_dark))
        }*/
        dataBinding.bloodPressureRange.text = "Min: $bloodPressureMin, Max: $bloodPressureMax"
        val adapter = object : SparkAdapter(){
            override fun getCount(): Int {return bloodPressureList.size}
            override fun getItem(index: Int): Any {return bloodPressureList[index]}
            override fun getY(index: Int): Float {return bloodPressureList[index].toFloat()}
        }
        dataBinding.bloodPressure.adapter = adapter

        dataBinding.addBloodPressureData.setOnClickListener {
            addNewData(0, bloodPressureList, splitParts)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setSugarFasting(Concat: String, splitParts: List<String>) {
        val splitSugarFasting = Concat.split(":")
        val sugarFastingList: ArrayList<Int> = ArrayList()
        for (i in 0..4) {
            sugarFastingList.add(Integer.parseInt(splitSugarFasting[i]))
        }
        val sugarFastingMin = min(sugarFastingList)
        val sugarFastingMax = max(sugarFastingList)
        dataBinding.sugarFastingRange.text = "Min: $sugarFastingMin, Max: $sugarFastingMax"

        val adapter = object : SparkAdapter(){
            override fun getCount(): Int {return sugarFastingList.size}
            override fun getItem(index: Int): Any {return sugarFastingList[index]}
            override fun getY(index: Int): Float {return sugarFastingList[index].toFloat()}
        }

        dataBinding.sugarFasting.adapter = adapter
        dataBinding.addSugarFastingData.setOnClickListener {
            addNewData(1, sugarFastingList, splitParts)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setSugarPP(Concat: String, splitParts: List<String>) {
        val splitSugarPP = Concat.split(":")
        val sugarPPList: ArrayList<Int> = ArrayList()
        for (i in 0..4) {
            sugarPPList.add(Integer.parseInt(splitSugarPP[i]))
        }
        val sugarPPMin = min(sugarPPList)
        val sugarPPMax = max(sugarPPList)
        dataBinding.sugarPPRange.text = "Min: $sugarPPMin, Max: $sugarPPMax"

        val adapter = object : SparkAdapter(){
            override fun getCount(): Int {return sugarPPList.size}
            override fun getItem(index: Int): Any {return sugarPPList[index]}
            override fun getY(index: Int): Float {return sugarPPList[index].toFloat()}
        }

        dataBinding.sugarPP.adapter = adapter

        dataBinding.addSugarPPData.setOnClickListener {
            addNewData(2, sugarPPList, splitParts)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setCholesterol(Concat: String, splitParts: List<String>) {
        val splitCholesterol = Concat.split(":")
        val cholesterolList: ArrayList<Int> = ArrayList()
        for (i in 0..4) {
            cholesterolList.add(Integer.parseInt(splitCholesterol[i]))
        }
        val cholesterolMin = min(cholesterolList)
        val cholesterolMax = max(cholesterolList)
        dataBinding.cholesterolRange.text = "Min: $cholesterolMin, Max: $cholesterolMax"

        val adapter = object : SparkAdapter(){
            override fun getCount(): Int {return cholesterolList.size}
            override fun getItem(index: Int): Any {return cholesterolList[index]}
            override fun getY(index: Int): Float {return cholesterolList[index].toFloat()}
        }

        dataBinding.cholesterol.adapter = adapter

        dataBinding.addCholesterolData.setOnClickListener {
            addNewData(3, cholesterolList, splitParts)
        }
    }

    /*private fun addData() {
        val list1 = mutableListOf<Int>()
        db.child("Users").child(userID).child("stats").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val data = snapshot.value as List<*>
                    for (i in data){
                        list1.add(i.toString().toInt())
                    }
                    Log.d("Data", "Data: $list1")
                    *//*val valueOfStats = snapshot.value.toString()
                    val regex = Regex("[^\\d,-]")
                    val cleanedValues = regex.replace(valueOfStats, "")
                    val valuesArray = cleanedValues.split(",").toTypedArray()
                    list1.addAll(valuesArray.map { it.toInt() })
                    Log.d("BloodData", "i value: $list1")
                    for (value in list1){
                        array.add(value)
                    }*//*
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
    }*/

    private fun addNewData(ind: Int, List: ArrayList<Int>, splitParts: List<String>) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireActivity())
        builder.setTitle("Title")

        // Set up the input
        val input = EditText(requireActivity())
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.hint = "Enter new data"
        input.inputType = InputType.TYPE_CLASS_NUMBER
        builder.setView(input)
        // Set up the buttons
        builder.setPositiveButton("Set", DialogInterface.OnClickListener { _, _ ->
            // Here you get get input text from the Edittext
            val newDataValue = input.text.toString().trim()

            val queue: LinkedList<Int> = LinkedList(List)
            queue.poll()
            queue.offer(Integer.parseInt(newDataValue))
            List.clear()
            for (i in 0..4) {
                List.add(queue.poll())
            }
            val editor = sharedPreferences.edit()
            val merged = mergedData(ind, List, splitParts)
            db.child("Users").child(userID).child("stats").setValue(merged)
            editor.putString("stats", merged)
            editor.apply()

            val fragmentId = findNavController().currentDestination?.id
            findNavController().popBackStack(fragmentId!!, true)
            findNavController().navigate(fragmentId)
        })
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.cancel() }
        builder.show()
    }

    private fun mergedData(i: Int, List: ArrayList<Int>, splitParts: List<String>): String {
        val tempString: StringBuilder = StringBuilder()
        for (j in 0..4) {
            tempString.append(List[j]).append(":")
        }
        tempString.setLength(tempString.length - 1)
        val finalStats: StringBuilder = StringBuilder()
        for (j in 0..3) {
            if (j == i) {
                finalStats.append(tempString).append("?")
            } else {
                finalStats.append(splitParts.get(j)).append("?")
            }
        }
        finalStats.setLength(finalStats.length - 1)
        return finalStats.toString()
    }

    override fun onStart() {
        super.onStart()
        Handler().postDelayed({
            getDataFromSharedPreference()
        }, 1000)
    }

    private fun getDataFromSharedPreference() {
        userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
        stats = sharedPreferences.getString("stats", "0:0:0:0:0?0:0:0:0:0?0:0:0:0:0?0:0:0:0:0").toString()
    }

}