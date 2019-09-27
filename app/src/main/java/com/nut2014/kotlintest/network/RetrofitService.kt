package com.nut2014.kotlintest.network

import com.nut2014.kotlintest.entity.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*


/**
 * Created by Administrator on 2018/1/20.
 * 自定义请求接口的方法
 */
interface RetrofitService {

    @POST("user/get")
    fun getUser(
        @Query("id") id: Int
    ): Observable<BaseResponse<User>>

    @POST("login")
    fun login(
        @Query("userName") tel: String, @Query("passWord") passWord: String
    ): Observable<BaseResponse<User>>

    @POST("cover/getUserPage")
    fun getUserPage(
        @Query("pageNum") pageNum: Int,@Query("userId") user_id: Int
    ): Observable<BasePageResponse<List<Cover>>>


    @POST("cover/getAllPage")
    fun getCovers(
        @Query("pageNum") pageNum: Int
    ): Observable<BasePageResponse<List<Cover>>>

    @Multipart
    @POST("file/upload")
    fun uploadImage(@Part file: MultipartBody.Part, @Part("name") requestBody: RequestBody): Observable<BasePageResponse<String>>


    @POST("cover/add")
    fun addCover(
       @Body cover: Cover
    ): Observable<BaseResponse<String>>

    @POST("app/getVersion")
    fun getVersion(): Observable<BaseResponse<AppVersion>>

    @POST("tag/getAll")
    fun getAllTag(): Observable<BaseResponse<List<MyTag>>>


    @POST("outLogin")
    fun outLogin(@Query("userId") userId: Int): Observable<BaseResponse<User>>


    @POST("user/updateUserInfo")
    fun updateUserInfo(@Body user: User): Observable<BaseResponse<String>>
}