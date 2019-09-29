package com.nut2014.kotlintest.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nut2014.kotlintest.base.MyApplication
import com.nut2014.kotlintest.entity.Cover
import java.util.*

object CacheUtils {
    fun saveCoverList(coverList: List<Cover>) {
        if (coverList.isNotEmpty()) {
            var subList = coverList
            if (subList.size > 10) {
                subList = coverList.subList(0, 10)
            }
            SpUtils.setString(MyApplication.application(), "cache", "pub_cover_list", Gson().toJson(subList))
        }

    }

    fun getCoverList(): List<Cover> {
        val string = SpUtils.getString(MyApplication.application(), "cache", "pub_cover_list")
        if (string.isNotEmpty()) {
            val founderListType = object : TypeToken<ArrayList<Cover>>() {}.type
            return Gson().fromJson<List<Cover>>(string, founderListType)
        }
        return emptyList()
    }


}
