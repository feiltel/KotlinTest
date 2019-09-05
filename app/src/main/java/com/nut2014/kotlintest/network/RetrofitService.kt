package com.nut2014.kotlintest.network

import com.nut2014.kotlintest.entity.BaseResponse
import com.nut2014.kotlintest.entity.User
import io.reactivex.Observable
import retrofit2.http.GET
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

    @GET("getUsers")
    fun getUsers(
        @Query("userName") tel: String, @Query("passWord") passWord: String
    ): Observable<BaseResponse<List<User>>>
}