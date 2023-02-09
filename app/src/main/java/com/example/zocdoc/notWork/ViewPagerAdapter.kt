package com.example.zocdoc.notWork


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.example.zocdoc.R


class ViewPagerAdapter(private val context: Context) : PagerAdapter() {
    private val image = arrayOf(R.drawable.on_board1, R.drawable.on_board2, R.drawable.on_board3, R.drawable.on_board4)
    private val heading = arrayOf(R.string.heading1, R.string.heading2, R.string.heading3, R.string.heading4)
    private val description = arrayOf(R.string.onboardDes1, R.string.onboardDes2, R.string.onboardDes3, R.string.onboardDes4)

    override fun getCount(): Int {
        return 4
    }

    override fun isViewFromObject(view: View, ob: Any): Boolean {
        return view == ob
    }

    @Deprecated("Deprecated in Java")
    override fun instantiateItem(container: View, position: Int): Any {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater.inflate(R.layout.slider_layout, null)

        val slideImage = view.findViewById<ImageView>(R.id.titleImage)
        val slideHeadline = view.findViewById<TextView>(R.id.texttitle)
        val slideDes = view.findViewById<TextView>(R.id.textdeccription)

        slideImage.setImageResource(image[position])
        slideHeadline.setText(heading[position])
        slideDes.setText(description[position])

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}


