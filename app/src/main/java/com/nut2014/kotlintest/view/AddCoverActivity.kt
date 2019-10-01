package com.nut2014.kotlintest.view


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.jaeger.library.StatusBarUtil
import com.leon.lfilepickerlibrary.LFilePicker
import com.linchaolong.android.imagepicker.ImagePicker
import com.linchaolong.android.imagepicker.cropper.CropImage
import com.linchaolong.android.imagepicker.cropper.CropImageView
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.base.BaseActivity
import com.nut2014.kotlintest.base.CommonConfig
import com.nut2014.kotlintest.base.MyApplication
import com.nut2014.kotlintest.entity.Cover
import com.nut2014.kotlintest.entity.MyTag
import com.nut2014.kotlintest.network.runRxLambda
import com.nut2014.kotlintest.utils.ImageUtils
import com.nut2014.kotlintest.utils.MusicUtils
import com.nut2014.kotlintest.utils.UserDataUtils
import kotlinx.android.synthetic.main.activity_add_cover.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.jetbrains.anko.toast
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URI

/**
 * 添加
 */
class AddCoverActivity : BaseActivity() {
    private var coverData: Cover? = null

    private var imagePicker: ImagePicker? = null
    private lateinit var uploadImgUrl: String
    private lateinit var uploadMusicFileUrl: String
    private var uploadMusicName: String = ""
    private var uploadMusicArtist: String = ""
    private var uploadMusicCoverPath: String = ""
    private var selectTagId: Int = 0
    private val musicFileRequestCode = 1000


    private lateinit var tagList: List<MyTag>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cover)

        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 0)
        setSupportActionBar(toolbar_tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        if (intent.hasExtra("cover")) {
            coverData = CommonConfig.fromJson(intent.getStringExtra("cover"), Cover::class.java)

            uploadImgUrl = coverData!!.coverImgPath
            uploadMusicFileUrl = coverData!!.coverMusicPath
            content_et.setText(coverData!!.coverDes)
            music_btn.setText(coverData!!.musicName)
            uploadMusicName = coverData!!.musicName
            uploadMusicArtist = coverData!!.artistName
            uploadMusicCoverPath = coverData!!.musicCoverPath
            selectTagId = coverData!!.tag_id

            Glide.with(this@AddCoverActivity).load(uploadImgUrl).into(photo_iv)
        }

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
        music_btn.setOnClickListener {

            LFilePicker()
                .withActivity(this)
                .withMutilyMode(false)//单选模式
                .withRequestCode(musicFileRequestCode)
                .withStartPath("/storage/emulated/0/netease/cloudmusic/Music/")//指定初始显示路径
                .withIsGreater(true)//过滤文件大小 小于指定大小的文件
                .withFileSize((5 * 1024).toLong())//指定文件大小为500K
                .start()
        }
    }

    private fun getTagRequest() {
        runRxLambda(MyApplication.application().getService().getAllTag(
        ), {
            val dataList = mutableListOf<String>()
            var selectPos = 0
            if (it.code == 1) {
                tagList = it.data
                tagList.forEachIndexed { index, myTag ->
                    dataList.add(myTag.name)
                    if (selectTagId > 0 && selectTagId == myTag.id) {
                        selectPos = index
                    }
                }

            }
            val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, dataList)
            tag_sp.adapter = arrayAdapter
            if (selectPos > 0) {
                tag_sp.setSelection(selectPos)
            }
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
        if (uploadImgUrl.isEmpty()) {
            toast("请先选择图片")
        }
        println(UserDataUtils.getId())
        println(uploadMusicFileUrl)
        var coverId = 0

        if (coverData != null && coverData!!.id > 0) {
            coverId = coverData!!.id
        }
        val cover =
            Cover(
                coverId,
                UserDataUtils.getId(),
                uploadImgUrl,
                uploadMusicFileUrl,
                content_et.text.toString(),
                0,
                "",
                "",
                "",
                uploadMusicName,
                uploadMusicArtist,
                uploadMusicCoverPath,
                selectTagId
            )
        runRxLambda(MyApplication.application().getService().addCover(
            cover
        ), {
            if (it.code == 1) {
                finish()
            }
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
        runRxLambda(MyApplication.application().getService().uploadImage(
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
        imagePicker?.onActivityResult(this, requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == musicFileRequestCode) {
                val list = data?.getStringArrayListExtra("paths")
                if (!list.isNullOrEmpty()) {
                    unloadMusicFile(File(list[0]))
                }

            }
        }
    }

    private fun uploadCoverImg(coverPicture: Bitmap) {
        val folder = Environment.getExternalStorageDirectory().toString() + File.separator + "nut2014/coverImg.jpeg"
        if (!File(folder).parentFile.exists()) {
            File(folder).parentFile.mkdirs()
        }
        val coverFile = File(folder)
        val bos = BufferedOutputStream(FileOutputStream(coverFile))
        coverPicture.compress(Bitmap.CompressFormat.JPEG, 90, bos)
        bos.flush()
        bos.close()
        runRxLambda(MyApplication.application().getService().uploadImage(
            ImageUtils.getPart(coverFile),
            RequestBody.create("text/plain".toMediaTypeOrNull(), "image-type")
        ), {
            if (isActive) {
                if (it.code == 1) {
                    uploadMusicCoverPath = it.data
                } else {
                    toast(it.msg)
                }
            }

        }, {

            it?.printStackTrace()


        })
    }

    private fun unloadMusicFile(file: File) {
        uploadMusicName = MusicUtils.getCoverTitle(file.path)
        uploadMusicArtist = MusicUtils.getCoverArtist(file.path)
        val coverPicture = MusicUtils.getCoverPicture(file.path)
        uploadCoverImg(coverPicture)



        runRxLambda(MyApplication.application().getService().uploadImage(
            ImageUtils.getPart(file),
            RequestBody.create("text/plain".toMediaTypeOrNull(), "image-type")
        ), {
            if (isActive) {
                if (it.code == 1) {
                    uploadMusicFileUrl = it.data
                    music_btn.setText(file.name)
                } else {
                    toast(it.msg)
                }
            }

        }, {

            it?.printStackTrace()


        })
    }

}
