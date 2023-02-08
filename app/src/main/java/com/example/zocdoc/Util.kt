package com.example.zocdoc

object Util {
    fun removeCountryCode(number: String) : String {
        val numLength = number.length
        return if (numLength > 10) {
            val startIndex: Int = numLength - 10
            val newNumber: String = number.substring(startIndex, numLength)
            newNumber
        } else {
            number
        }
    }
}