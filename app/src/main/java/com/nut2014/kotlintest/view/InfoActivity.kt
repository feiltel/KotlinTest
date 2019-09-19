package com.nut2014.kotlintest.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.utils.GlideImageLoader
import kotlinx.android.synthetic.main.activity_info.*
import java.util.ArrayList

class InfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)
        init()
    }

    /**
     * 初始化
     */
    private fun init() {
        list_rv.setImageLoader(GlideImageLoader())

        val paths = ArrayList<String>()
        paths.add("http://192.168.31.196:8080/image/default_avator.png")
        paths.add("http://192.168.31.196:8080/image/default_avator.png")
        paths.add("http://192.168.31.196:8080/image/default_avator.png")
        paths.add("http://192.168.31.196:8080/image/default_avator.png")
        list_rv!!.setImages(paths)
        list_rv.start()


    }
}
