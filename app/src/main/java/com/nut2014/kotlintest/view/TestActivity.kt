package com.nut2014.kotlintest.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.utils.UserDataUtils

class TestActivity : AppCompatActivity() {
    private val type = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        showSelect()

    }

    private fun showSelect() {
        val abc = if (type > 0) UserDataUtils.getId() else 0

    }


}
