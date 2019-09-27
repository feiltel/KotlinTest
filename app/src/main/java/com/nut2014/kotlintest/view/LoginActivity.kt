package com.nut2014.kotlintest.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.base.BaseApplication
import com.nut2014.kotlintest.network.runRxLambda
import com.nut2014.kotlintest.utils.PermissionUtils
import com.nut2014.kotlintest.utils.UrlUtils
import com.nut2014.kotlintest.utils.UserDataUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_btn.setOnClickListener {
            login(username_et.text.toString(), userpass_et.text.toString())
        }


    }

    /**
     * 登录
     */
    private fun login(userName: String = "", passWord: String = "") {
        if (checkLogin(userName, passWord)) {
            loginAct(userName, passWord)
        }
    }

    /**
     * 检查登录字符
     */
    private fun checkLogin(userName: String, passWord: String): Boolean {
        if (userName.isEmpty() || passWord.isEmpty()) {
            toast("用户名密码不能为空")
            return false
        }
        return true
    }


    /**
     * 执行登录
     */
    private fun loginAct(userName: String, passWord: String) {
        runRxLambda(BaseApplication.App().getService().login(userName, passWord), {
            toast(it.msg)
            if (it.code == 1) {
                UserDataUtils.saveUser(it.data)
                toast("登录成功")
                setResult(1)
                finish()
            }
        }, {
            it?.printStackTrace()
            toast("fail")
        })
    }
}
