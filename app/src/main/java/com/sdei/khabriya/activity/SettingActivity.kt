package com.sdei.khabriya.activity

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.sdei.khabriya.R
import com.sdei.khabriya.utils.Utilities
import kotlinx.android.synthetic.main.setting_layout.*


class SettingActivity : AppCompatActivity() {
    var versionTv: TextView? = null
    var txtPrefe: RelativeLayout? = null
    var notificationSwitch: Switch? = null
    var preferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null
    var backImg: ImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.setting_layout)
        setIds()
        var pInfo: PackageInfo? = null
        try {
            pInfo = packageManager.getPackageInfo(packageName, 0)
            val version = pInfo.versionName
            versionTv!!.text = version
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        if (!preferences!!.getBoolean(Utilities.ENABLE_NOTI, false)) {
            notificationSwitch!!.isChecked = true
        } else {
            notificationSwitch!!.isChecked = false
        }

        notificationSwitch!!.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            if (isChecked) {
                editor!!.putBoolean(Utilities.ENABLE_NOTI, false).commit()
                Log.e("noti", "true")
            } else {
                editor!!.putBoolean(Utilities.ENABLE_NOTI, true).commit()
                Log.e("noti", "false")
            }
        }
        backImg!!.setOnClickListener { finish() }
        txtPrefe!!.setOnClickListener { v: View? ->
            val intent =
                Intent(this, PreferenceActivity::class.java)
            startActivity(intent)
        }

        txtShare.setOnClickListener {

//            Hey! Download Khabriya app on your phone for free to read lastest news in your language. Click to Download
//            https://khabriya.in

            try {
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "khabriya")
                var shareMessage = "\nLet me recommend you this application\n\n"
                shareMessage ="Hey! Download Khabriya app on your phone for free to read lastest news in your language. Click to Download https://khabriya.in"
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                startActivity(Intent.createChooser(shareIntent, "choose one"))
            } catch (e: Exception) {
                //e.toString();
            }

        }
        txtReport.setOnClickListener {

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("hello@khabriya.in"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Channel not working")

            try {
                startActivity(Intent.createChooser(intent, "Send mail..."))
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    this@SettingActivity,
                    "There are no email clients installed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    override fun onBackPressed() {
        finish()
    }

    private fun setIds() {
        versionTv = findViewById<View>(R.id.version_tv) as TextView
        notificationSwitch =
            findViewById<View>(R.id.notification_switch) as Switch
        txtPrefe = findViewById<View>(R.id.txtPrefe) as RelativeLayout
        backImg = findViewById<View>(R.id.back_img) as ImageView
        preferences = getSharedPreferences(
            Utilities.PREFS,
            Context.MODE_PRIVATE
        )
        editor = preferences!!.edit()
    }

    override fun onDestroy() {
        super.onDestroy()
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }
}