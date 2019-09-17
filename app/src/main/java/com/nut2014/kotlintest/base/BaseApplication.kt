package com.nut2014.kotlintest.base

import android.app.Application
import android.util.Log
import com.nut2014.kotlintest.network.RetrofitService
import okhttp3.OkHttpClient.Builder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class BaseApplication : Application() {
    private lateinit var retrofitService: RetrofitService

    companion object {
        private lateinit var instance: BaseApplication
        fun App(): BaseApplication {
            return instance
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initRetrofit()
    }

    fun initRetrofit() {
      /*  val build = Builder().connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
        })
        logging.level = HttpLoggingInterceptor.Level.BODY

        build.addNetworkInterceptor(logging)*/

        retrofitService = Retrofit.Builder()
         //   .client(build.build())
            .baseUrl("http://123.206.39.150:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(RetrofitService::class.java)
    }

    fun getService(): RetrofitService {
        return retrofitService
    }
}