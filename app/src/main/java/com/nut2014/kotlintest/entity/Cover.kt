package com.nut2014.kotlintest.entity

data class Cover(
    val user_id: Int,
    val coverImgPath: String,
    val coverDes: String,
    val likeNumber: Int,
    val avatarPath: String,
    val userName: String,
    val tagName: String,
    val tag_id:Int
)