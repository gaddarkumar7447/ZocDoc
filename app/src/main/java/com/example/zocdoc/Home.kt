package com.example.zocdoc

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.zocdoc.databinding.ActivityMainBinding

class Home : AppCompatActivity() {
    private lateinit var troggle: ActionBarDrawerToggle
    private lateinit var dataBinding: ActivityMainBinding

    private var GFG_URI = "https://gaddarkumar7447.github.io/My-Portfolio/"
    var package_name = "com.android.chrome"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //actionBarSetUp()
        setupFragments()

        //showActionBar()
    }

    private fun showActionBar() {

    }

    /*private fun actionBarSetUp() {
        setSupportActionBar(dataBinding.toolBar)
        troggle = ActionBarDrawerToggle(this, dataBinding.drawerLayout, R.string.app_name, R.string.app_name)
        dataBinding.drawerLayout.addDrawerListener(troggle)
        troggle.syncState()
    }*/

    private fun setupFragments() {
        val bottomNavigationView = dataBinding.bottomNav
        val navController : NavController = findNavController(R.id.fragmentContainerView)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFragment, R.id.statisticFragment, R.id.appointmentFragment, R.id.settingFragment))

        //setupActionBarWithNavController(navController, appBarConfiguration)

        /*navController.addOnDestinationChangedListener(NavController.OnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.homeFragment -> dataBinding.appBar.visibility = View.VISIBLE
                else -> dataBinding.appBar.visibility = View.INVISIBLE
            }
        })*/

        bottomNavigationView.setupWithNavController(navController)
    }

    @Deprecated("Deprecated in Java", ReplaceWith("finishAffinity()"))
    override fun onBackPressed() {
        finishAffinity()
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (troggle.onOptionsItemSelected(item)) {

            return true
        }
        return super.onOptionsItemSelected(item)
    }*/
}



















/*dataBinding.urlButtom.setOnClickListener {
            *//*val builder = CustomTabsIntent.Builder()
            builder.setToolbarColor(ContextCompat.getColor(this, R.color.dull_blue))
            builder.addDefaultShareMenuItem()
            builder.setShowTitle(true)
            builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)
            builder.setShareState(CustomTabsIntent.SHARE_STATE_DEFAULT)

            val anotherCustom = CustomTabsIntent.Builder().build()
            val custom = builder.build()
            custom.launchUrl(this, Uri.parse(GFG_URI))*//*

            val builder = CustomTabsIntent.Builder()
            val params = CustomTabColorSchemeParams.Builder()
            params.setToolbarColor(ContextCompat.getColor(this, R.color.dull_blue))
            builder.setDefaultColorSchemeParams(params.build())
            builder.setShowTitle(true)
            builder.setShareState(CustomTabsIntent.SHARE_STATE_ON)
            builder.setShareState(CustomTabsIntent.SHARE_STATE_DEFAULT)
            builder.setInstantAppsEnabled(true)
            val customBuilder = builder.build()

            try {
                customBuilder.launchUrl(this, Uri.parse(GFG_URI))
            }catch (e : Exception){
                Toast.makeText(this, "Some  error", Toast.LENGTH_SHORT).show()
            }

            *//*try {
                if (this.isPackageInstalled(packageName)) {
                    customBuilder.intent.setPackage(packageName)
                    customBuilder.launchUrl(this, Uri.parse(GFG_URI))
                } else {
                    Toast.makeText(this, "Some error", Toast.LENGTH_LONG).show()
                }
            }catch (e : Exception){
                Log.d("Ex", "exception: "+e)
                Toast.makeText(this, ""+e, Toast.LENGTH_LONG).show()
            }*//*
        }*/

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