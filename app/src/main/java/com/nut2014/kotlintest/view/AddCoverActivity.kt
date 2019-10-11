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

    private var imagePicker: ImagePicker? = null
    private var uploadImgUrl: String = ""
    private var uploadImgUrl2: String = ""
    private var uploadImgUrl3: String = ""
    private var uploadImgUrl4: String = ""
    private lateinit var uploadMusicFileUrl: String
    private var uploadMusicName: String = ""
    private var uploadMusicArtist: String = ""
    private var uploadMusicCoverPath: String = ""
    private var selectTagId: Int = 0
    private val musicFileRequestCode = 1000

    private var coverId = 0


    private lateinit var tagList: List<MyTag>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cover)
        StatusBarUtil.setColor(this, ContextCompat.getColor(this, R.color.colorPrimary), 0)
        setSupportActionBar(toolbar_tb)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        //判断是否是编辑
        if (intent.hasExtra("cover")) {
            coverId = intent.getIntExtra("cover", 0)
            getCoverInfo(coverId)
        } else {
            getTagRequest()
        }
        //图片点击
        photo_iv.setOnClickListener {
            showPicSelect(1)
        }

        photo_iv2.setOnClickListener {
            showPicSelect(2)
        }
        photo_iv3.setOnClickListener {
            showPicSelect(3)
        }
        photo_iv4.setOnClickListener {
            showPicSelect(4)
        }
        //分类选择点击事件
        tag_sp!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                selectTagId = tagList[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
        //选择音乐按钮
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

    //编辑时恢复界面 数据
    private fun setView(coverData: Cover) {
        val split = coverData!!.coverImgPath.split(",")
        if (split.isNotEmpty()) {
            uploadImgUrl = split[0]
            if (split.size > 1) {
                uploadImgUrl2 = split[1]
            }
            if (split.size > 2) {
                uploadImgUrl3 = split[2]
            }
            if (split.size > 3) {
                uploadImgUrl4 = split[3]

            }
        }



        uploadMusicFileUrl = coverData!!.coverMusicPath
        content_et.setText(coverData!!.coverDes)
        music_btn.setText(coverData!!.musicName)
        uploadMusicName = coverData!!.musicName
        uploadMusicArtist = coverData!!.artistName
        uploadMusicCoverPath = coverData!!.musicCoverPath
        selectTagId = coverData!!.tag_id
        getTagRequest()


        if (uploadImgUrl.isNotEmpty()) {
            Glide.with(this@AddCoverActivity).load(uploadImgUrl).into(photo_iv)
        }
        if (uploadImgUrl2.isNotEmpty()) {
            Glide.with(this@AddCoverActivity).load(uploadImgUrl2).into(photo_iv2)
        }
        if (uploadImgUrl3.isNotEmpty()) {
            Glide.with(this@AddCoverActivity).load(uploadImgUrl3).into(photo_iv3)
        }
        if (uploadImgUrl4.isNotEmpty()) {
            Glide.with(this@AddCoverActivity).load(uploadImgUrl4).into(photo_iv4)
        }
    }

    //根据ID获取专辑数据
    private fun getCoverInfo(id: Int) {
        runRxLambda(MyApplication.application().getService().getCoverInfo(
            id
        ), {
            if (it.code == 1) {
                setView(it.data)
            }
        }, {

        })
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

    private fun getAllPicUrl(): String {
        return "$uploadImgUrl,$uploadImgUrl2,$uploadImgUrl3,$uploadImgUrl4"
    }

    private fun saveCover() {
        if (uploadImgUrl.isEmpty()) {
            toast("请先选择图片")
        }
        println(UserDataUtils.getId())
        println(uploadMusicFileUrl)


        val cover =
            Cover(
                coverId,
                UserDataUtils.getId(),
                getAllPicUrl(),
                uploadMusicFileUrl,
                content_et.text.toString(),
                0,
                "",
                "",
                "",
                uploadMusicName,
                uploadMusicArtist,
                uploadMusicCoverPath,
                selectTagId,
                1
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


    private fun showPicSelect(picPos: Int) {

        imagePicker = ImagePicker()
        imagePicker!!.setCropImage(true)

        imagePicker!!.startChooser(this, object : ImagePicker.Callback() {
            override fun onPickImage(imageUri: Uri) {

            }

            override fun onCropImage(imageUri: Uri?) {
                super.onCropImage(imageUri)
                unloadPic(
                    File(URI(imageUri.toString())), picPos
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

    private fun unloadPic(file: File, picPos: Int) {
        runRxLambda(MyApplication.application().getService().uploadImage(
            ImageUtils.getPart(file),
            RequestBody.create("text/plain".toMediaTypeOrNull(), "image-type")
        ), {
            if (it.code == 1) {


                when (picPos) {
                    1 -> Glide.with(this@AddCoverActivity).load(it.data).into(photo_iv)
                    2 -> Glide.with(this@AddCoverActivity).load(it.data).into(photo_iv2)
                    3 -> Glide.with(this@AddCoverActivity).load(it.data).into(photo_iv3)
                    4 -> Glide.with(this@AddCoverActivity).load(it.data).into(photo_iv4)
                }

                when (picPos) {
                    1 -> uploadImgUrl = it.data
                    2 -> uploadImgUrl2 = it.data
                    3 -> uploadImgUrl3 = it.data
                    4 -> uploadImgUrl4 = it.data
                }

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
