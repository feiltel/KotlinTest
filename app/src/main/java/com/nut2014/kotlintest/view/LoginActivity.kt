package com.nut2014.kotlintest.view

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.base.MyApplication
import com.nut2014.kotlintest.network.runRxLambda
import com.nut2014.kotlintest.utils.UserDataUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    private var isRegistered: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setViews(isRegistered)
        login_btn.setOnClickListener {
            login(username_et.text.toString(), userpass_et.text.toString())
        }
        check_type_btn.setOnClickListener {
            isRegistered = !isRegistered
            setViews(isRegistered)
        }
        close_im.setOnClickListener {
            finish()
        }


    }

    private fun setViews(isRegistered: Boolean) {
        if (isRegistered) {
            repeat_userpass_et.visibility = View.VISIBLE
            title_tv.text = resources.getString(R.string.user_Registered)
            login_btn_title.text = resources.getString(R.string.registered)
            check_type_btn.text = resources.getString(R.string.login)
        } else {
            repeat_userpass_et.visibility = View.GONE
            title_tv.text = resources.getString(R.string.user_login)
            login_btn_title.text = resources.getString(R.string.login)
            check_type_btn.text = resources.getString(R.string.registered)
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
            toast(getString(R.string.username_password_cannot_empty))
            return false
        }
        if (isRegistered) {
            if (repeat_userpass_et.text.toString() != userpass_et.text.toString()) {
                toast(getString(R.string.Iconsistent_password_entered_twice))
                return false
            }
        }
        return true
    }


    /**
     * 执行登录
     */
    private fun loginAct(userName: String, passWord: String) {
        var login = MyApplication.application().getService().login(userName, passWord)
        if (isRegistered) {
            login = MyApplication.application().getService().registered(userName, passWord)
        }
        runRxLambda(login, {
            toast(it.msg)
            if (it.code == 1) {
                UserDataUtils.saveUser(it.data)
                toast(it.msg)
                setResult(1)
                finish()
            }
        }, {
            it?.printStackTrace()
            toast("fail")
        })
    }
}
