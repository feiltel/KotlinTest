package com.nut2014.kotlintest.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.utils.PermissionUtils
import com.nut2014.kotlintest.utils.UrlUtils
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toast(UrlUtils.baseIP())
        jumpHomeActivity()
        finish()

    }

    /**
     * 跳转到主页
     */
    private fun jumpHomeActivity() {
        startActivity(Intent(this, HomeActivity::class.java))
    }
}
