package com.nut2014.kotlintest.entity

data class BaseResponse<T>(val code:Int,val msg:String,val data:T)