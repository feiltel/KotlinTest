package com.nut2014.kotlintest.utils

import android.app.Activity
import android.content.Intent
import com.nut2014.kotlintest.base.MyApplication
import com.nut2014.kotlintest.entity.User
import com.nut2014.kotlintest.view.LoginActivity
import org.jetbrains.anko.toast

object UserDataUtils {
    fun saveUser(user: User) {
        SpUtils.setString(MyApplication.application(), "user", "name", user.userName)
        SpUtils.setString(MyApplication.application(), "user", "pass", user.passWord)
        SpUtils.setfInt(MyApplication.application(), "user", "id", user.id)
        SpUtils.setString(MyApplication.application(), "user", "token", user.token)
        SpUtils.setString(MyApplication.application(), "user", "BgImg", user.bgImg)
        SpUtils.setString(MyApplication.application(), "user", "userIcon", user.avatarPath)

    }

    fun getId(): Int {
        return SpUtils.getInt(MyApplication.application(), "user", "id")
    }

    fun getUserName(): String {
        return SpUtils.getString(MyApplication.application(), "user", "name")
    }

    fun getUserPass(): String {
        return SpUtils.getString(MyApplication.application(), "user", "pass")
    }

    fun getToken(): String {
        return SpUtils.getString(MyApplication.application(), "user", "token")
    }

    fun getBgImg(): String {
        return SpUtils.getString(MyApplication.application(), "user", "BgImg")
    }

    fun getAvatarPath(): String {
        return SpUtils.getString(MyApplication.application(), "user", "userIcon")
    }

    fun isLoginAndJump(context: Activity): Boolean {

        if (getId() > 0 && getToken().isNotEmpty()) {
            return true
        } else {
            context.toast("请先登录")
            context.startActivityForResult(Intent(context, LoginActivity::class.java), Constant.loginRequstCode)
            return false
        }
    }
    fun isLogin(): Boolean {
        return getId() > 0 && getToken().isNotEmpty()
    }
    fun jumpLogin(currentActivity: Activity) {
        currentActivity.startActivityForResult(
            Intent(currentActivity, LoginActivity::class.java),
            Constant.loginRequstCode
        )
    }

}
