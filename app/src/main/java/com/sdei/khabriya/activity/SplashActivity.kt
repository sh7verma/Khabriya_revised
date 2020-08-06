package com.sdei.khabriya.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sdei.khabriya.R
import com.sdei.khabriya.utils.MySharedPreferences

class SplashActivity : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH: Long = 1000

    private fun startTimer() {
        Handler().postDelayed({
            if (MySharedPreferences.getInstance(this@SplashActivity)
                    .getString(MySharedPreferences.Key.CATEGORIES_CHOSEN).isNullOrBlank()
            ) {
                val intent = Intent(this, PreferenceActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Log.i(
                    "MainActivity",
                    "" + MySharedPreferences.getInstance(this@SplashActivity).getString(
                        MySharedPreferences.Key.CATEGORIES_CHOSEN
                    )
                )
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

        }, SPLASH_DISPLAY_LENGTH)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startTimer()

    }
}