package com.example.zocdoc

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.example.zocdoc.databinding.ActivityMainBinding


class Home : AppCompatActivity() {
    private lateinit var dataBinding: ActivityMainBinding

    private var GFG_URI = "https://gaddarkumar7447.github.io/My-Portfolio/"
    var package_name = "com.android.chrome"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide()


        dataBinding.urlButtom.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(this, R.color.dull_blue))
            builder.addDefaultShareMenuItem()
            builder.setShowTitle(true)
            builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)
            builder.setShareState(CustomTabsIntent.SHARE_STATE_DEFAULT)

            val anotherCustom = CustomTabsIntent.Builder().build()
            val custom = builder.build()
            custom.launchUrl(this, Uri.parse(GFG_URI))

            /*val builder = CustomTabsIntent.Builder()
            val params = CustomTabColorSchemeParams.Builder()
            params.setToolbarColor(ContextCompat.getColor(this, R.color.dull_blue))
            builder.setDefaultColorSchemeParams(params.build())
            builder.setShowTitle(true)
            builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)
            builder.setShareState(CustomTabsIntent.SHARE_STATE_DEFAULT)
            builder.setInstantAppsEnabled(true)
            val customBuilder = builder.build()

            try {
                if (this.isPackageInstalled(packageName)) {
                    customBuilder.intent.setPackage(packageName)
                    customBuilder.launchUrl(this, Uri.parse(GFG_URI))
                } else {
                    Toast.makeText(this, "Some error", Toast.LENGTH_LONG).show()
                }
            }catch (e : Exception){
                Log.d("Ex", "exception: "+e)
                Toast.makeText(this, ""+e, Toast.LENGTH_LONG).show()
            }*/
        }

        /*fun openCustomTab(activity: Activity, customTabsIntent: CustomTabsIntent, uri: Uri?) {
            val packageName = "com.example.zocdoc"
            if (packageName != null) {
                customTabsIntent.intent.setPackage(packageName)
                customTabsIntent.launchUrl(activity, uri!!)
            } else {
                activity.startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        }

        fun Context.isPackageInstalled(packageName: String): Boolean {
            return try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    packageManager.getPackageInfo(packageName, 0)
                } else {

                }

                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }*/
    }

    @Deprecated("Deprecated in Java", ReplaceWith("finishAffinity()"))
    override fun onBackPressed() {
        finishAffinity()
    }
}