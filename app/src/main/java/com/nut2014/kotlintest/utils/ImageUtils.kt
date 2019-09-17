package com.nut2014.kotlintest.utils

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody

import java.io.File

object ImageUtils {
    fun getPart(file: File): MultipartBody.Part {
        val fileReqBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        // Create MultipartBody.Part using file request-body,file name and part name
        return MultipartBody.Part.createFormData("file", file.name, fileReqBody)
    }
}
