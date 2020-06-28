package com.nut2014.baselibrary.uitls

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.DisplayMetrics

/**
 * Created by Administrator on 2016/11/21 0021.
 */
object DeviceUtils {
    fun getScreenWidth(activity: Activity): Int {
        val metric = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metric)
        return metric.widthPixels
    }

    fun getScreenHeight(activity: Activity): Int {
        val metric = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metric)
        return metric.heightPixels
    }

    fun getDp(activity: Activity, old: Int): Int {
        return (old * getScreenDensity(activity)).toInt()
    }

    fun getScreenDensity(activity: Activity): Float {
        val metric = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(metric)
        return metric.density
    }

    fun callPhone(context: Context, phoneNum: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:$phoneNum")
        intent.data = data
        context.startActivity(intent)
    }
}