package com.nut2014.baselibrary.uitls

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider

import java.io.File

/**
 * Created by Administrator on 2016/12/6 0006.
 */

object AppUtils {
    //得到版本名称
    fun getVersionName(context: Context): String {
        val manager = context.packageManager
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }

    fun getVersionCode(context: Context): Int {
        val manager = context.packageManager
        try {
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return 0
    }

    fun getManiApplicationValue(context: Context, key: String): String? {
        try {
            val appInfo = context.packageManager
                .getApplicationInfo(
                    context.packageName,
                    PackageManager.GET_META_DATA
                )
            return appInfo.metaData.getString(key)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return ""
    }


    fun installApk(context: Activity?, apkFile: File, providerStr: String) {
        if (context == null) {
            return
        }
        if (!apkFile.exists()) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val hasInstallPermission = context.packageManager.canRequestPackageInstalls()
            if (!hasInstallPermission) {
                ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.REQUEST_INSTALL_PACKAGES), 20)
            }
        }

        val i = Intent(Intent.ACTION_VIEW)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            val apkUri = FileProvider.getUriForFile(context, providerStr, apkFile)
            i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            i.setDataAndType(apkUri, "application/vnd.android.package-archive")
        } else {
            i.setDataAndType(Uri.parse("file://$apkFile"), "application/vnd.android.package-archive")
        }

        context.startActivity(i)
    }

}
