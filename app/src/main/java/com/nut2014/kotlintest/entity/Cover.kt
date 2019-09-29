package com.nut2014.kotlintest.entity

import com.nut2014.kotlintest.base.CommonConfig

data class Cover(
    val id: Int,
    val user_id: Int,
    val coverImgPath: String,
    val coverMusicPath: String,
    val coverDes: String,
    val likeNumber: Int,
    val avatarPath: String,
    val userName: String,
    val tagName: String,
    val tag_id: Int
) : CommonConfig()