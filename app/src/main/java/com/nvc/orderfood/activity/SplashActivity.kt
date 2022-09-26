package com.nvc.orderfood.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.nvc.orderfood.databinding.ActivitySplashBinding


class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            img.animate().translationY(-2600f).setDuration(1000).startDelay = 2000
            logo.animate().translationY(2000f).setDuration(1000).startDelay = 2000
            brand.animate().translationY(2000f).setDuration(1000).startDelay = 2000
            splash.animate().translationY(2000f).setDuration(1000).startDelay = 2000
        }
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
//            overridePendingTransition(R.anim.slide_out_bottom, R.anim.no_anim)
            finish()
        }, 1700)
    }
}