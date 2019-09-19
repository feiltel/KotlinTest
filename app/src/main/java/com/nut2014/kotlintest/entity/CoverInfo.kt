package com.nut2014.kotlintest.entity

data class CoverInfo(
    val user_id: String,
    val coverImgPath: String,
    val coverDes: String,
    val likeNumber: Int,
    val avatarPath: String,
    val userName: String,
    val tagName: String
)