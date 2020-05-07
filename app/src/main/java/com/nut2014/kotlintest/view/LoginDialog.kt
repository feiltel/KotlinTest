package com.nut2014.kotlintest.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.nut2014.baselibrary.uitls.DeviceUtils
import com.nut2014.eventbuslib.FunctionManager
import com.nut2014.eventbuslib.annotation.FunctionBusManager
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.base.MyApplication
import com.nut2014.kotlintest.network.runRxLambda
import com.nut2014.kotlintest.utils.UserDataUtils
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.support.v4.toast

/**
 * 到站地址选择对话框
 * Created by Administrator on 2017/3/10 0010.
 */

class LoginDialog : DialogFragment() {
    private var isRegistered: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return inflater.inflate(R.layout.activity_login, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViews(isRegistered)
        login_btn.setOnClickListener {
            login(username_et.text.toString(), userpass_et.text.toString())
        }
        check_type_btn.setOnClickListener {
            isRegistered = !isRegistered
            setViews(isRegistered)
        }
        close_im.setOnClickListener {

             dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        //设置窗口宽度和屏幕宽度一致
        val dm = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(dm)
        dialog!!.window!!.setLayout(DeviceUtils.getDp(activity, 300), DeviceUtils.getDp(activity, 500))
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
                dismiss()
                FunctionManager.getInstance().invokeFunction("loginCallback_cover")
                FunctionManager.getInstance().invokeFunction("loginCallback_home")
            }
        }, {
            it?.printStackTrace()
            toast("fail")
        })
    }

}
