package com.example.zocdoc.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.example.zocdoc.R
import com.example.zocdoc.auth.LogInActivity
import com.example.zocdoc.databinding.ActivityOnBindingBinding
import com.google.android.material.tabs.TabLayoutMediator
class OnBindingActivity : AppCompatActivity() {
    private lateinit var mViewPager: ViewPager2
    private lateinit var dataBinding : ActivityOnBindingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_binding)
        supportActionBar?.hide()

        mViewPager = dataBinding.viewPager
        mViewPager.adapter = OnBindingAdapter(this, this)

        TabLayoutMediator(dataBinding.pageIndicator, mViewPager) { _, _ -> }.attach()

        dataBinding.textSkip.setOnClickListener {
            startActivity(Intent(applicationContext, LogInActivity::class.java))
            finish()
        }

        dataBinding.btnNextStep.setOnClickListener {
            if (getItem() - 1 > mViewPager.childCount) {
                startActivity(Intent(applicationContext, LogInActivity::class.java))
                finish()
            } else {
                mViewPager.setCurrentItem(getItem() + 1, true)
            }
        }
    }

    private fun getItem(): Int {
        return mViewPager.currentItem
    }
}