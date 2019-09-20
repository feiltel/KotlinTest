package com.nut2014.kotlintest.view


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.linchaolong.android.imagepicker.ImagePicker
import com.linchaolong.android.imagepicker.cropper.CropImage
import com.linchaolong.android.imagepicker.cropper.CropImageView
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.base.BaseApplication
import com.nut2014.kotlintest.entity.Cover
import com.nut2014.kotlintest.entity.MyTag
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
    private var selectTagId: Int = 0


    private lateinit var tagList: List<MyTag>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cover)
        setSupportActionBar(toolbar_tb)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        photo_iv.setOnClickListener {
            showPicSelect()
        }

        getTagRequest()
        tag_sp!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectTagId = tagList[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun getTagRequest() {
        runRxLambda(BaseApplication.App().getService().getAllTag(
        ), {
            val dataList = mutableListOf<String>()
            if (it.code == 1) {
                tagList = it.data
                for (datum in tagList) {
                    dataList.add(datum.name)
                }
            }
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dataList)
            tag_sp.adapter = arrayAdapter
        }, {
            toast(it?.message.toString())
            it?.printStackTrace()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item!!.itemId
        if (itemId == android.R.id.home) {
            onBackPressed()
        } else if (itemId == R.id.save_btn) {
            saveCover()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveCover() {

        val cover =
            Cover(UserDataUtils.getId(this@AddCoverActivity), uploadImgUrl, content_et.text.toString(), 0, "", "", "",selectTagId)
        runRxLambda(BaseApplication.App().getService().addCover(
            cover
        ), {
            toast(it.msg)
        }, {
            toast(it?.message.toString())
            it?.printStackTrace()
        })
    }


    private fun showPicSelect() {

        imagePicker = ImagePicker()
        imagePicker!!.setCropImage(true)
        imagePicker!!.startChooser(this, object : ImagePicker.Callback() {
            override fun onPickImage(imageUri: Uri) {

            }

            override fun onCropImage(imageUri: Uri?) {
                super.onCropImage(imageUri)
                unloadPic(
                    File(URI(imageUri.toString()))
                )
            }

            override fun cropConfig(builder: CropImage.ActivityBuilder) {
                builder
                    // 是否启动多点触摸
                    .setMultiTouchEnabled(true)
                    // 设置网格显示模式
                    .setGuidelines(CropImageView.Guidelines.OFF)
                    // 圆形/矩形
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    // 调整裁剪后的图片最终大小
                    .setRequestedSize(1200, 900)
                    // 宽高比
                    .setAspectRatio(4, 3)
            }
        })


    }

    private fun unloadPic(file: File) {
        runRxLambda(BaseApplication.App().getService().uploadImage(
            ImageUtils.getPart(file),
            RequestBody.create("text/plain".toMediaTypeOrNull(), "image-type")
        ), {
            if (it.code == 1) {
                Glide.with(this@AddCoverActivity).load(it.data).into(photo_iv)
                uploadImgUrl = it.data
            } else {
                toast(it.msg)
            }

        }, {

            it?.printStackTrace()


        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagePicker!!.onActivityResult(this, requestCode, resultCode, data)
    }

}