package com.nut2014.kotlintest.base

import com.google.gson.Gson

/**
 * JSON 转换工具类
 */
open class CommonConfig {

    companion object {

        fun <T> fromJson(json: String, clazz: Class<T>): T {
            return Gson().fromJson(json, clazz)
        }
    }

    fun toJson(): String {
        return Gson().toJson(this)
    }
}