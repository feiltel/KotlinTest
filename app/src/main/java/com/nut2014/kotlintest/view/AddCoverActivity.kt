package com.nut2014.kotlintest.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.linchaolong.android.imagepicker.ImagePicker
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.base.BaseApplication
import com.nut2014.kotlintest.network.runRxLambda
import com.nut2014.kotlintest.utils.ImageUtils
import com.nut2014.kotlintest.utils.UserDataUtils
import kotlinx.android.synthetic.main.activity_add_cover.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.jetbrains.anko.toast
import java.io.File
import java.net.URI


class AddCoverActivity : AppCompatActivity() {
    private var imagePicker: ImagePicker? = null
    private lateinit var uploadImgUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cover)
        photo_iv.setOnClickListener {
            showPicSelect()
        }
        save_btn.setOnClickListener {
            saveCover()
        }
    }

    private fun saveCover() {
        runRxLambda(BaseApplication.App().getService().addCover(
          UserDataUtils.getId(this@AddCoverActivity),uploadImgUrl,content_et.text.toString()
        ), {
            toast(it.msg)
        }, {

            it?.printStackTrace()


        })
    }


    private fun showPicSelect() {
        imagePicker = ImagePicker()
        imagePicker!!.setCropImage(false)
        imagePicker!!.startChooser(this, object : ImagePicker.Callback() {
            override fun onPickImage(imageUri: Uri) {
                unloadPic(
                    File(URI(imageUri.toString()))
                )

            }
        })
    }

    private fun unloadPic(file: File) {
        runRxLambda(BaseApplication.App().getService().uploadImage(
            ImageUtils.getPart(file),
            RequestBody.create("text/plain".toMediaTypeOrNull(), "image-type")
        ), {
            Glide.with(this@AddCoverActivity).load(it.data).into(photo_iv)
            uploadImgUrl = it.data
        }, {

            it?.printStackTrace()


        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagePicker!!.onActivityResult(this, requestCode, resultCode, data)
    }

}
