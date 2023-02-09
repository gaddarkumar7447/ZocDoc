package com.example.zocdoc.notWork

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.example.zocdoc.R
import com.example.zocdoc.auth.LogInActivity
import com.example.zocdoc.databinding.ActivityOnBoardingScreenBinding


class OnBoardingScreen : AppCompatActivity() {
    private lateinit var dataBinding : ActivityOnBoardingScreenBinding
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var dots : Array<TextView?> = arrayOfNulls(4)
    private lateinit var linearLayout: LinearLayout

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityOnBoardingScreenBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)
        // dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_on_boarding_screen)

        supportActionBar?.hide()
        dataBinding.backbtn.setOnClickListener{
            Log.d("Button", "Button is working")
            if (getitem(0) > 0){
                viewPager.setCurrentItem(getitem(-1),true)
            }
        }

        dataBinding.nextbtn.setOnClickListener{
            if (getitem(0) < 4){
                viewPager.setCurrentItem(getitem(1),true)
            }else {
                startActivity(Intent(this, LogInActivity::class.java))
                finish()
            }
        }

        dataBinding.skipButton.setOnClickListener{
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

        viewPager = dataBinding.slideViewPager
        linearLayout = findViewById(R.id.indicator_layout)
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter

        viewPager.addOnPageChangeListener(viewListener)

        /*mSLideViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        mDotLayout = (LinearLayout) findViewById(R.id.indicator_layout);
        viewPagerAdapter = new ViewPagerAdapter(this);
        mSLideViewPager.setAdapter(viewPagerAdapter);
        setUpindicator(0);
        mSLideViewPager.addOnPageChangeListener(viewListener);*/

        setUpIndicator(0)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setUpIndicator(position : Int) {
        dots = arrayOf(TextView(this), TextView(this), TextView(this), TextView(this))
        linearLayout.removeAllViews()

        for (i in dots.indices){
            dots[i] = TextView(this);
            dots[i]?.text = Html.fromHtml("&#8226")
            dots[i]?.textSize = 35F
            dots[i]?.setTextColor(resources.getColor(R.color.light_blue, applicationContext.theme))
            linearLayout.addView(dots[i])
        }
        dots[position]?.setTextColor(resources.getColor(R.color.light_blue, applicationContext.theme))
    }

    private var viewListener : OnPageChangeListener = object : OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onPageSelected(position: Int) {
            setUpIndicator(position)
            if (position > 0) {
                dataBinding.backbtn.visibility = View.VISIBLE
            } else {
                dataBinding.backbtn.visibility = View.INVISIBLE
            }
        }

        override fun onPageScrollStateChanged(state: Int) {}
    }

    private fun getitem(i: Int): Int {
        return viewPager.currentItem + i
    }
}