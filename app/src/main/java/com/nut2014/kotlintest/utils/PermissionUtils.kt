package com.nut2014.kotlintest.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import java.util.ArrayList

object PermissionUtils {


    fun hasPermission(context: Activity, permissionStr: String): Boolean {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(
            context,
            permissionStr
        )
    }

    fun requestPermissions(context: Activity, permissions: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(context, permissions, requestCode)
    }

    //统一请求所有权限
    fun checkAll(context: Activity) {
        val permissionStr = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        val mPermissionList = ArrayList<String>()
        for (i in permissionStr.indices) {
            if (ContextCompat.checkSelfPermission(context, permissionStr[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissionStr[i])
            }
        }
        if (!mPermissionList.isEmpty()) {
            val permissions = mPermissionList.toTypedArray()//将List转为数组
            ActivityCompat.requestPermissions(context, permissions, 0)
        }
    }
}
