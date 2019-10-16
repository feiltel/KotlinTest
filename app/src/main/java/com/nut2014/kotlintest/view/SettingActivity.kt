package com.nut2014.kotlintest.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.refactor.lib.colordialog.PromptDialog
import com.jaeger.library.StatusBarUtil
import com.linchaolong.android.imagepicker.ImagePicker
import com.linchaolong.android.imagepicker.cropper.CropImage
import com.linchaolong.android.imagepicker.cropper.CropImageView
import com.nut2014.baselibrary.uitls.ImageUtils
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.base.MyApplication
import com.nut2014.kotlintest.entity.User
import com.nut2014.kotlintest.network.getUploadFileService
import com.nut2014.kotlintest.network.runRxLambda
import com.nut2014.kotlintest.utils.UpdateUtils
import com.nut2014.kotlintest.utils.UserDataUtils
import kotlinx.android.synthetic.main.activity_setting.*
import org.jetbrains.anko.toast
import java.io.File
import java.net.URI

class SettingActivity : AppCompatActivity() {
    private var imagePicker: ImagePicker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTransparent(this)
        setContentView(R.layout.activity_setting)

        ImageUtils.loadCircleImg(this, UserDataUtils.getAvatarPath(), user_icon_iv)


        user_name_tv.text = UserDataUtils.getUserName()
        out_login_cv.setOnClickListener {
            showOutDialog()
        }
        user_icon_iv.setOnClickListener {
            showPicSelect(true)
        }
        cover_bg_iv.setOnClickListener {
            showPicSelect(false)
        }
        ImageUtils.loadImg(this, UserDataUtils.getBgImg(), cover_iv)

        about_tv.setOnClickListener {
            showAboutDialog()
        }
        check_update_tv.setOnClickListener {
            UpdateUtils(this).checkVersion(true)
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


    private fun unloadPic(file: File, isUserIcon: Boolean) {
        runRxLambda(getUploadFileService(file), {
            if (it.code == 1) {
                if (isUserIcon) {
                    updateUserInfo(isUserIcon, it.data)
                    ImageUtils.loadCircleImg(this@SettingActivity, it.data, user_icon_iv)
                } else {

                    ImageUtils.loadImg(this@SettingActivity, it.data, cover_iv)
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
        runRxLambda(MyApplication.application().getService().updateUserInfo(
            user
        ), {
            toast(it.msg)

        }, {

            it?.printStackTrace()


        })
    }

    private fun showAboutDialog() {
        PromptDialog(this)
            .setDialogType(PromptDialog.DIALOG_TYPE_INFO)
            .setAnimationEnable(true)
            .setTitleText("关于瞬间")
            .setContentText("关于瞬间关于瞬间关于瞬间关于瞬间关于瞬间关于瞬间关于瞬间关于瞬间关于瞬间关于瞬间关于瞬间关于瞬间关于瞬间关于瞬间关于瞬间关于瞬间关于瞬间")
            .setPositiveListener("确定") { dialog ->
                dialog.dismiss()
            }.show()
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
            MyApplication.application().getService().outLogin(
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
