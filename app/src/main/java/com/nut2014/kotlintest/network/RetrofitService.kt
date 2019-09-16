package com.nut2014.kotlintest.network

import com.nut2014.kotlintest.entity.BasePageResponse
import com.nut2014.kotlintest.entity.BaseResponse
import com.nut2014.kotlintest.entity.Cover
import com.nut2014.kotlintest.entity.User
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Part
import retrofit2.http.Query

/**
 * Created by Administrator on 2018/1/20.
 * 自定义请求接口的方法
 */
interface RetrofitService {

    @GET("getUser")
    fun loadOrg(
        @Query("id") tel: String
    ): Observable<User>

    @GET("login")
    fun login(
        @Query("userName") tel: String, @Query("passWord") passWord: String
    ): Observable<BaseResponse<User>>

    @GET("cover/getAllPage")
    fun getCovers(
        @Query("pageNum") pageNum: Int
    ): Observable<BasePageResponse<List<Cover>>>

    @GET("file/upload")
    fun uploadImg(
        @Part("file") partList: List<MultipartBody.Part>
    ): Observable<BasePageResponse<String>>
}