package com.example.liargame.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.TextView
import com.example.liargame.R

/**
 * 재밌다 키키키키키키키키ㅣ
 */
class SplashActivity : AppCompatActivity() {

    private lateinit var startButton : TextView

    // 다른 액티비티에서도 사용할 수 있도록 하는 object
    companion object {
        var WRITER : String =""
    }

    /**
     * onCreate()
     *
     * Setting it's own Activity Data
     *
     * TODO
     * 1. lateinit value binding
     * 2. button animation task
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()

        startButton = findViewById<TextView>(R.id.activity_splash_start_button)

        Handler(Looper.getMainLooper()).postDelayed({
            startButton.animate().alpha(1f).setDuration(300).start()
        }, 3000)
    }

    /**
     * onStart()
     *
     * Write up initial logics and settings of this Activity
     *
     * TODO
     * 1. writer textview text setting
     */
    override fun onStart() {
        super.onStart()
        findViewById<TextView>(R.id.activity_splash_writer).text = ""
    }

    /**
     * onResume()
     *
     * Write up logics of this Activity
     * When User returns this Activity, this lifecycle will be called
     *
     * TODO
     * 1. button clickable flag binding
     * 2. click event regist
     */
    override fun onResume() {
        super.onResume()

        startButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}