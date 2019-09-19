package com.nut2014.kotlintest.network

import com.nut2014.kotlintest.entity.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


/**
 * Created by Administrator on 2018/1/20.
 * 自定义请求接口的方法
 */
interface RetrofitService {

    @POST("getUser")
    fun loadOrg(
        @Query("id") tel: String
    ): Observable<User>

    @POST("login")
    fun login(
        @Query("userName") tel: String, @Query("passWord") passWord: String
    ): Observable<BaseResponse<User>>

    @POST("cover/getAllPage")
    fun getCovers(
        @Query("pageNum") pageNum: Int
    ): Observable<BasePageResponse<List<Cover>>>

    @Multipart
    @POST("file/upload")
    fun uploadImage(@Part file: MultipartBody.Part, @Part("name") requestBody: RequestBody): Observable<BasePageResponse<String>>


    @POST("cover/add")
    fun addCover(
        @Query("user_id") user_id: Int, @Query("coverImgPath") coverImgPath: String, @Query("coverDes") coverDes: String
    ): Observable<BaseResponse<String>>

    @POST("app/getVersion")
    fun getVersion(): Observable<BaseResponse<AppVersion>>
}