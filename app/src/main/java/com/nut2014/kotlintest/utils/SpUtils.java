package com.nut2014.kotlintest.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by admin on 2015/8/10.
 */
public class SpUtils {

    public static String getString(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    public static void setString(Context context, String fileName, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        sp.edit().putString(key, value).apply();
    }

    public static boolean getBoolean(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        return sp.getBoolean(key, false);
    }

    public static void setBoolean(Context context, String fileName, String key, boolean defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        sp.edit().putBoolean(key, defaultValue).apply();
    }

    public static int getInt(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        return sp.getInt(key, 0);
    }

    public static void setfInt(Context context, String fileName, String key, int defaultValue) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        sp.edit().putInt(key, defaultValue).apply();
    }

    public static void remove(Context context, String fileName, String key) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }

    public static void clear(Context context, String fileName) {
        SharedPreferences sp = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE);
        sp.edit().clear().apply();
    }


}

