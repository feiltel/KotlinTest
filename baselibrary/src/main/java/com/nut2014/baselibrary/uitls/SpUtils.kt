package com.nut2014.baselibrary.uitls

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

/**
 * Created by admin on 2015/8/10.
 */
object SpUtils {

    fun getString(context: Context, fileName: String, key: String): String {
        val sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE)
        if (sp.getString(key, "")==null){
            return ""
        }
        return sp.getString(key, "")!!
    }

    fun setString(context: Context, fileName: String, key: String, value: String) {
        val sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE)
        sp.edit().putString(key, value).apply()
    }

    fun getBoolean(context: Context, fileName: String, key: String): Boolean {
        val sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE)
        return sp.getBoolean(key, false)
    }

    fun setBoolean(context: Context, fileName: String, key: String, defaultValue: Boolean) {
        val sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE)
        sp.edit().putBoolean(key, defaultValue).apply()
    }

    fun getInt(context: Context, fileName: String, key: String): Int {
        val sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE)
        return sp.getInt(key, 0)
    }

    fun setfInt(context: Context, fileName: String, key: String, defaultValue: Int) {
        val sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE)
        sp.edit().putInt(key, defaultValue).apply()
    }

    fun remove(context: Context, fileName: String, key: String) {
        val sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE)
        sp.edit().remove(key).apply()
    }

    fun clear(context: Context, fileName: String) {
        val sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE)
        sp.edit().clear().apply()
    }


}

