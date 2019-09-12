package com.nut2014.kotlintest.entity

data class BasePageResponse<T>(val code:Int, val msg:String, val total:Int,val pageNum:Int,val pages:Int,val data:T)