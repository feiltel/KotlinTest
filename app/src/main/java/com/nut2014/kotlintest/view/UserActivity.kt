package com.nut2014.kotlintest.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import cn.refactor.lib.colordialog.PromptDialog
import com.bumptech.glide.Glide
import com.linchaolong.android.imagepicker.ImagePicker
import com.linchaolong.android.imagepicker.cropper.CropImage
import com.linchaolong.android.imagepicker.cropper.CropImageView
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.base.BaseApplication
import com.nut2014.kotlintest.entity.User
import com.nut2014.kotlintest.network.runRxLambda
import com.nut2014.kotlintest.utils.ImageUtils
import com.nut2014.kotlintest.utils.UserDataUtils
import kotlinx.android.synthetic.main.activity_user.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import org.jetbrains.anko.toast
import java.io.File
import java.net.URI

class UserActivity : AppCompatActivity() {
    private var imagePicker: ImagePicker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        setSupportActionBar(toolbar_tb)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        Glide.with(this).load(UserDataUtils.getAvatarPath())
            .into(user_icon_iv)

        user_name_tv.setText(UserDataUtils.getUserName())
        out_login_cv.setOnClickListener {
            showOutDialog()
        }
        user_icon_cv.setOnClickListener {
            showPicSelect(true)
        }
        cover_bg_iv.setOnClickListener {
            showPicSelect(false)
        }
    }

    private fun showPicSelect(isUserIcon: Boolean) {

        imagePicker = ImagePicker()
        imagePicker!!.setCropImage(true)
        imagePicker!!.setTitle(if (isUserIcon) "选择头像" else "选择主页背景")
        imagePicker!!.startChooser(this, object : ImagePicker.Callback() {
            override fun onPickImage(imageUri: Uri) {

            }

            override fun onCropImage(imageUri: Uri?) {
                super.onCropImage(imageUri)
                unloadPic(
                    File(URI(imageUri.toString())), isUserIcon
                )
            }

            override fun cropConfig(builder: CropImage.ActivityBuilder) {
                if (isUserIcon) {
                    builder
                        // 是否启动多点触摸
                        .setMultiTouchEnabled(true)
                        // 设置网格显示模式
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        // 圆形/矩形
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        // 调整裁剪后的图片最终大小
                        .setRequestedSize(500, 500)
                        // 宽高比
                        .setAspectRatio(1, 1)
                } else {
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
            }
        })


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item!!.itemId
        if (itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun unloadPic(file: File, isUserIcon: Boolean) {
        runRxLambda(BaseApplication.App().getService().uploadImage(
            ImageUtils.getPart(file),
            RequestBody.create("text/plain".toMediaTypeOrNull(), "image-type")
        ), {
            if (it.code == 1) {
                if (isUserIcon) {
                    updateUserInfo(isUserIcon, it.data)
                    Glide.with(this@UserActivity).load(it.data).into(user_icon_iv)
                } else {
                    updateUserInfo(isUserIcon, it.data)
                }

            } else {
                toast(it.msg)
            }

        }, {

            it?.printStackTrace()


        })
    }

    private fun updateUserInfo(isUserIcon: Boolean, imgUrl: String) {
        var user = User("", "", "", UserDataUtils.getId(), "", imgUrl, "")
        if (isUserIcon) {
            user = User("", "", "", UserDataUtils.getId(), "", "", imgUrl)
        }
        runRxLambda(BaseApplication.App().getService().updateUserInfo(
            user
        ), {
            toast(it.msg)

        }, {

            it?.printStackTrace()


        })
    }


    private fun showOutDialog() {
        PromptDialog(this)
            .setDialogType(PromptDialog.DIALOG_TYPE_WARNING)
            .setAnimationEnable(true)
            .setTitleText("退出登录")
            .setContentText("确定要退出登录吗")
            .setPositiveListener("确定") { dialog ->
                dialog.dismiss()
                outLogin()
            }
            .show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        imagePicker!!.onActivityResult(this, requestCode, resultCode, data)
    }


    private fun outLogin() {
        runRxLambda(
            BaseApplication.App().getService().outLogin(
                UserDataUtils.getId()
            ), {
                if (it.code == 1) {
                    UserDataUtils.saveUser(User("", "", "", 0, "", "", ""))
                    toast(it.msg)
                    finish()
                }
            }, {
                toast(it?.message.toString())
                it?.printStackTrace()
            })
    }
}
