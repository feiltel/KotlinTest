package com.nut2014.kotlintest.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionUtils {


    public static boolean hasPermission(Activity context, String permissionStr) {
        return PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(context,
                permissionStr);
    }

    public static void requestPermissions(Activity context,String[] permissions,int requestCode){
        ActivityCompat.requestPermissions(context, permissions, requestCode);
    }

    //统一请求所有权限
    public static void checkAll(Activity context) {
        String[] permissionStr = new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION
        };

        List<String> mPermissionList = new ArrayList<>();
        for (int i = 0; i < permissionStr.length; i++) {
            if (ContextCompat.checkSelfPermission(context, permissionStr[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissionStr[i]);
            }
        }
        if (!mPermissionList.isEmpty()) {
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(context, permissions, 0);
        }
    }
}
