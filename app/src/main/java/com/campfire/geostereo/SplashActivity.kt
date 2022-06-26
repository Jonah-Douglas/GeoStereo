package com.campfire.geostereo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper


/**
 *  Activity to show a quick Splash screen for GeoStereo App
 */
class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long = 500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            finish()
        }, SPLASH_TIME_OUT)
    }
}