package com.nut2014.kotlintest.utils

import android.content.Context
import com.nut2014.kotlintest.base.BaseApplication
import com.nut2014.kotlintest.entity.User

object UserDataUtils {
    fun saveUser(context: Context, user: User) {
        SpUtils.setString(context, "user", "name", user.userName)
        SpUtils.setString(context, "user", "pass", user.passWord)
        SpUtils.setfInt(context, "user", "id", user.id)
        SpUtils.setString(context, "user", "token", user.token)
        SpUtils.setString(context, "user", "BgImg", user.bgImg)
    }
    fun getId(): Int {
        return SpUtils.getInt(BaseApplication.App(), "user", "id")
    }
    fun getUserName(): String {
        return SpUtils.getString(BaseApplication.App(), "user", "name")
    }

    fun getUserPass(): String {
        return SpUtils.getString(BaseApplication.App(), "user", "pass")
    }

    fun getToken(): String {
        return SpUtils.getString(BaseApplication.App(), "user", "token")
    }
    fun getBgImg(): String {
        return SpUtils.getString(BaseApplication.App(), "user", "BgImg")
    }
}
