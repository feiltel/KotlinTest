package com.nut2014.kotlintest.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.base.BaseApplication
import com.nut2014.kotlintest.network.runRxLambda
import kotlinx.android.synthetic.main.activity_add_cover.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddCoverActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cover)

        link_btn.setOnClickListener {
            showPic()
        }
    }

    private fun showPic() {

    }
}
