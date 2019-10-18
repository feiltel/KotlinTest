package com.nut2014.kotlintest.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jaeger.library.StatusBarUtil
import com.nut2014.kotlintest.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTransparent(this)
        setContentView(R.layout.activity_main)

    }


}
