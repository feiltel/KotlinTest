package com.nut2014.kotlintest.utils

import android.app.Activity
import android.content.Intent
import com.nut2014.kotlintest.base.BaseApplication
import com.nut2014.kotlintest.entity.User
import com.nut2014.kotlintest.view.LoginActivity
import org.jetbrains.anko.toast

object UserDataUtils {
    fun saveUser(user: User) {
        SpUtils.setString(BaseApplication.App(), "user", "name", user.userName)
        SpUtils.setString(BaseApplication.App(), "user", "pass", user.passWord)
        SpUtils.setfInt(BaseApplication.App(), "user", "id", user.id)
        SpUtils.setString(BaseApplication.App(), "user", "token", user.token)
        SpUtils.setString(BaseApplication.App(), "user", "BgImg", user.bgImg)
        SpUtils.setString(BaseApplication.App(), "user", "userIcon", user.avatarPath)

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

    fun getAvatarPath(): String {
        return SpUtils.getString(BaseApplication.App(), "user", "userIcon")
    }

    fun isLoginAndJump(context: Activity): Boolean {

        if (getId() > 0 && getToken().isNotEmpty()) {
            return true
        } else {
            context.toast("请先登录")
            context.startActivityForResult(Intent(context, LoginActivity::class.java), Contant.loginRequstCode)
            return false
        }
    }
}
