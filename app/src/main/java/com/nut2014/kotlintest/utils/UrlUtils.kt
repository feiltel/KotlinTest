package com.nut2014.kotlintest.utils

import android.content.pm.PackageManager
import com.nut2014.kotlintest.base.MyApplication

object UrlUtils {

    private val REMOTE_IP = "http://123.206.39.150:8080/"
    private val LOCAL_IP = "http://192.168.31.196:8080/"

    private val channel: String?
        get() {
            try {
                val pm = MyApplication.application().packageManager
                val appInfo = pm.getApplicationInfo(MyApplication.application().packageName, PackageManager.GET_META_DATA)
                return appInfo.metaData.getString("BUILD_CHANNEL")
            } catch (ignored: PackageManager.NameNotFoundException) {
            }

            return ""
        }

    fun baseIP(): String {
        return if (channel == "test") {
            LOCAL_IP
        } else {
            REMOTE_IP
        }
    }
}
