package com.nut2014.kotlintest.base

import android.app.Application
import com.nut2014.kotlintest.BuildConfig
import com.nut2014.kotlintest.network.RetrofitService
import com.nut2014.kotlintest.utils.UrlUtils
import com.nut2014.kotlintest.utils.UserDataUtils
import com.wanjian.cockroach.Cockroach
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
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
        Cockroach.init(instance, null, null)
        initRetrofit()
    }

    fun initRetrofit() {


        val build = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            //打印网络请求日志
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            build.addInterceptor(httpLoggingInterceptor)
        }

        val logging = object : Interceptor {

            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {

                val original = chain.request()

                val request = original.newBuilder()

                    .header("token", UserDataUtils.getToken())

                    .method(original.method, original.body)

                    .build()

                return chain.proceed(request)

            }

        }

        build.addInterceptor(logging)

        retrofitService = Retrofit.Builder()
            .client(build.build())
            .baseUrl(UrlUtils.baseIP())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(RetrofitService::class.java)
    }

    fun getService(): RetrofitService {
        return retrofitService
    }
}