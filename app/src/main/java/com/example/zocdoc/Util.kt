package com.example.zocdoc

import android.app.Activity
import android.content.Context
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class Util {
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

    // create bottom sheet dialog
    fun createBottomSheet(activity: Activity): BottomSheetDialog {
        return BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)
    }

    fun setBottomSheet(view : View, bottomSheet: BottomSheetDialog) {
        bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheet.setContentView(view)
        bottomSheet.create()
        bottomSheet.show()
    }

}