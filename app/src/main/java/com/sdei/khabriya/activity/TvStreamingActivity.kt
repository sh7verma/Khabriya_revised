package com.sdei.khabriya.activity

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.sdei.khabriya.R
import kotlinx.android.synthetic.main.activity_tv_streaming.*

class TvStreamingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_tv_streaming)

        backTv.setOnClickListener {
            finish()
        }
        andExoPlayerView.setSource(intent.getStringExtra("url"))
        andExoPlayerView.setShowFullScreen(true)
    }


    override fun onPause() {
        super.onPause()
        andExoPlayerView.pausePlayer()
    }

}