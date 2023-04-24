package com.example.swifttext.ui.presentation

import android.app.Activity
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.swifttext.R
import com.example.swifttext.data.receiver.MyBroadCastReceiver
import com.example.swifttext.data.receiver.OtpReceiver
import com.example.swifttext.service.AuthService
import com.example.swifttext.utils.Constants
import com.example.swifttext.utils.NotificationUtils
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.material.navigation.NavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var navController: NavController
    private lateinit var authService: AuthService
    lateinit var myOtpReceiver: OtpReceiver
    lateinit var myReceiver: MyBroadCastReceiver
    val NOTIFICATION_REQ_CODE = 0
    private val FOREGROUND_REQ_CODE = 1
    private val ALARM_REQ_CODE = 2

    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main)
        val actionBar = supportActionBar
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#03256C")))
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setDisplayShowCustomEnabled(true)
        actionBar?.setCustomView(R.layout.action_bar)

        val navView: NavigationView = findViewById(R.id.navView)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawerLayout)
        val headerView = navView.getHeaderView(0)
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navController = Navigation.findNavController(this, R.id.navHostFragment)

        FirebaseApp.initializeApp(this)

        val auth = FirebaseAuth.getInstance()

        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("users")

        authService = AuthService(auth, ref)



        if (authService.isAuthenticate()) {
            val option = NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build()

            navController.navigate(R.id.homeFragment, null, option)

        }

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> {

                    val option = NavOptions.Builder()
                        .setPopUpTo(R.id.homeFragment, true)
                        .build()

                    navController.navigate(R.id.homeFragment, null, option)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.nav_logout -> {
                    logout()
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
            true
        }

        registerBroadCastReceiver()
        registerOtpReceiver()

    }


    private fun registerOtpReceiver() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (it.resultCode == Activity.RESULT_OK) {
                    val data = it.data
                    data?.let {
                        val msg = it.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE).toString()
                        val otp = Regex("\\d{4,6}").find(msg)?.value ?: ""
                    }
                }
            }

        myOtpReceiver = OtpReceiver()

        OtpReceiver.bind(object : OtpReceiver.Companion.Listener {
            override fun onSuccess(messageIntent: Intent) {
                resultLauncher.launch(messageIntent)
            }

            override fun onFailure() {

            }
        }
        )

        val otpFilter = IntentFilter()
        otpFilter.addAction(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(myOtpReceiver, otpFilter)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerBroadCastReceiver() {
        NotificationUtils.createNotificationChannel(this)
        checkPermission(
            "android.permission.POST_NOTIFICATIONS",
            NOTIFICATION_REQ_CODE
        )
        checkPermission(
            "android.permission.FOREGROUND_SERVICE",
            FOREGROUND_REQ_CODE
        )
        checkPermission(
            " android.permission.SCHEDULE_EXACT_ALARM",
            ALARM_REQ_CODE
        )

        val filter = IntentFilter()
        filter.addAction("com.example.MyBroadcast")

        myReceiver = MyBroadCastReceiver()
        registerReceiver(myReceiver, filter)


    }


    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), requestCode)
        } else {
            Log.d(Constants.DEBUG, "Permission is granted already")
        }
    }


    private fun logout() {
        authService.deAuthenticate()
        navController.navigate(R.id.loginFragment)
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//        when (requestCode) {
//            NOTIFICATION_REQ_CODE -> {
//                makeToast("Notification permission granted")
//            }
//
//            FOREGROUND_REQ_CODE -> {
//                makeToast("Foreground service permission granted")
//            }
//
//            ALARM_REQ_CODE -> {
//                makeToast("Notification permission granted")
//            }
//
//            else -> {
//                makeToast("Permission Denied")
//            }
//        }
//    }

//    private fun makeToast(reply: String) {
//        return Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
//    }
}