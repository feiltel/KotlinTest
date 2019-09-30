package com.nut2014.kotlintest.utils

import android.app.Activity
import android.content.Context
import cn.refactor.lib.colordialog.PromptDialog
import com.nut2014.kotlintest.BuildConfig
import com.nut2014.kotlintest.base.MyApplication
import com.nut2014.kotlintest.entity.AppVersion
import com.nut2014.kotlintest.network.runRxLambda
import org.jetbrains.anko.toast
import java.io.File


class UpdateUtils(private val context: Context) {
    fun checkVersion() {
        runRxLambda(
            MyApplication.application().getService().getVersion(), {
                if (it.code == 1) {
                    val data = it.data
                    if (data.versionCode > AppUtils.getVersionCode(context)) {
                        showDialog(data)
                    }
                }
            }, {
                it?.printStackTrace()
            })
    }

    private fun showDialog(data: AppVersion) {
        PromptDialog(context)
            .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
            .setAnimationEnable(true)
            .setTitleText("有新版本" + data.versionName)
            .setContentText(data.updateInfo)
            .setPositiveListener("更新") { dialog ->
                dialog.dismiss()
                downloadFile(data.downloadPath)
            }.show()
    }

    private fun downloadFile(downloadPath: String) {
        DownloadFile(context, object : DownloadFile.DownloadCallBack {
            override fun success(path: String) {
                context.toast(path)
                val apkfile = File(path)
                if (!apkfile.exists()) {
                    return
                }
                AppUtils.installApk(context as Activity?, apkfile, BuildConfig.APPLICATION_ID + ".imagePicker.provider")
            }

            override fun error(msg: String) {

            }

            override fun progress(progress: Int) {

            }
        }).execute(downloadPath)
    }


}
