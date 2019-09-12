package com.nut2014.kotlintest.utils

import android.content.Context
import com.nut2014.kotlintest.entity.User

object UserDataUtils {
    fun saveUser(context: Context, user: User) {
        SpUtils.setString(context, "user", "name", user.userName)
        SpUtils.setString(context, "user", "pass", user.passWord)
    }

    fun getUserName(context: Context): String {
        return SpUtils.getString(context, "user", "name")
    }

    fun getUserPass(context: Context): String {
        return SpUtils.getString(context, "user", "pass")
    }
}
