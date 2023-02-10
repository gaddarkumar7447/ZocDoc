package com.example.zocdoc

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
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

    @SuppressLint("ServiceCast")
    fun checkForInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

}