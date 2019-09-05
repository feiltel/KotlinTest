package com.nut2014.kotlintest.test

import com.nut2014.kotlintest.test.MyInterface

class Personal: MyInterface {
    override fun run() {
        println("人在跑")
    }

    override fun talk(mess: String): String {
        println("人在讲话：$mess")
        return ">>>>>>>>"
    }

}