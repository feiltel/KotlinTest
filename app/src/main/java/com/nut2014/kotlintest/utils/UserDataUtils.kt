package com.nut2014.kotlintest.utils

import android.content.Context
import com.nut2014.kotlintest.entity.User

object UserDataUtils {
    fun saveUser(context: Context, user: User) {
        SpUtils.setString(context, "user", "name", user.userName)
        SpUtils.setString(context, "user", "pass", user.passWord)
        SpUtils.setfInt(context, "user", "id", user.id)
        SpUtils.setString(context, "user", "token", user.token)
    }
    fun getId(context: Context): Int {
        return SpUtils.getInt(context, "user", "id")
    }
    fun getUserName(context: Context): String {
        return SpUtils.getString(context, "user", "name")
    }

    fun getUserPass(context: Context): String {
        return SpUtils.getString(context, "user", "pass")
    }

    fun getToken(context: Context): String {
        return SpUtils.getString(context, "user", "token")
    }
}
