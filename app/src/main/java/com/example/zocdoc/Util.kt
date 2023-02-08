package com.example.zocdoc

import android.app.Activity
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

    fun createBottomSheet(activity: Activity): BottomSheetDialog {
        return BottomSheetDialog(activity, R.style.BottomSheetDialogTheme)
    }

    fun View.setBottomSheet(bottomSheet: BottomSheetDialog) {
        bottomSheet.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheet.setContentView(this)
        bottomSheet.create()
        bottomSheet.show()
    }

}