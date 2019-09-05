package com.nut2014.kotlintest.test

class Animal: MyInterface {
    override fun run() {
        println("动物在跑")
    }

    override fun talk(mess: String): String {
        println("动物在讲话：$mess")
        return ">>>>>>>>"
    }

}