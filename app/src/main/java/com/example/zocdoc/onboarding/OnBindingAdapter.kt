package com.example.zocdoc.onboarding

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.zocdoc.R

class OnBindingAdapter(fragment : FragmentActivity, private val context: Context) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return 4
    }


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnBindingFragments.newInstance(
                context.resources.getString(R.string.heading1),
                context.resources.getString(R.string.onboardDes1),
                R.drawable.on_board1
            )
            1 -> OnBindingFragments.newInstance(
                context.resources.getString(R.string.heading2),
                context.resources.getString(R.string.onboardDes2),
                R.drawable.on_board2
            )
            2 ->  OnBindingFragments.newInstance(
                context.resources.getString(R.string.heading3),
                context.resources.getString(R.string.onboardDes3),
                R.drawable.on_board3
            )
            else -> OnBindingFragments.newInstance(
                context.resources.getString(R.string.heading4),
                context.resources.getString(R.string.onboardDes4),
                R.drawable.on_board4
            )
        }
    }
}