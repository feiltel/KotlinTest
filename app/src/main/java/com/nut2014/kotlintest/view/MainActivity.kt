package com.nut2014.kotlintest.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.nut2014.kotlintest.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler().postDelayed({
            jumpHomeActivity()
            finish()
        }, 1000)


    }

    /**
     * 跳转到主页
     */
    private fun jumpHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
    }
}
