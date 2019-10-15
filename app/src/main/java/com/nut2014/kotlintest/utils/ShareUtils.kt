package com.nut2014.kotlintest.utils

import android.content.Context
import android.content.Intent
import com.nut2014.baselibrary.uitls.UriUtils
import com.nut2014.kotlintest.BuildConfig
import java.io.File

/**
 * @author feiltel 2019/10/15 0015
 */
object ShareUtils {
    fun share(context: Context, title: String = "share", text: String = "", subject: String = "", file: File) {
        var shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND//设置分享行为
        shareIntent.type = "text/plain"//设置分享内容的类型
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)//添加分享内容标题
        shareIntent.putExtra(Intent.EXTRA_TEXT, text)//添加分享内容
        shareIntent.putExtra(
            Intent.EXTRA_STREAM,
            UriUtils.file2Uri(context, file, BuildConfig.APPLICATION_ID + ".imagePicker.provider")
        )
        // 指定发送内容的类型 (MIME type)
        shareIntent.type = "image/jpeg"
        //创建分享的Dialog
        shareIntent = Intent.createChooser(shareIntent, title)
        context.startActivity(shareIntent)

    }
}
