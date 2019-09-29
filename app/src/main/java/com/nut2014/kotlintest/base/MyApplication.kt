package com.nut2014.kotlintest.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.nut2014.kotlintest.BuildConfig
import com.nut2014.kotlintest.entity.BaseResponse
import com.nut2014.kotlintest.network.RetrofitService
import com.nut2014.kotlintest.utils.DeviceUuidFactory
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
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit


/**
 * 自定义的Application
 */
class MyApplication : Application(), Application.ActivityLifecycleCallbacks {


    private lateinit var retrofitService: RetrofitService
    private lateinit var deviceUuidFactory: DeviceUuidFactory

    companion object {
        private lateinit var instance: MyApplication
        fun application(): MyApplication {
            return instance
        }
    }


    override fun onCreate() {
        super.onCreate()
        deviceUuidFactory = DeviceUuidFactory(this)
        instance = this
        Cockroach.init(instance, null, null)
        initRetrofit()
        registerActivityLifecycleCallbacks(this)
    }

    fun getService(): RetrofitService {
        return retrofitService
    }

    /**
     * 初始化Retrofit
     */
    private fun initRetrofit() {
        //配置网络请求超时时间
        val build = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            //打印网络请求日志
            val httpLoggingInterceptor = HttpLoggingInterceptor()
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BASIC
            build.addInterceptor(httpLoggingInterceptor)
        }

        //定义一个拦截器
        val logging = object : Interceptor {

            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): Response {
                val original = chain.request()
                //每个请求头加入token
                val request = original.newBuilder()
                    .header("token", UserDataUtils.getToken())
                    .header("deviceId", deviceUuidFactory.deviceUuid.toString())
                    .method(original.method, original.body)
                    .build()
                val response = chain.proceed(request)
                //拦截相应结果
                interceptLogin(response)
                return response

            }

        }
        //添加拦截器
        build.addInterceptor(logging)
        //初始化retrofitService
        retrofitService = Retrofit.Builder()
            .client(build.build())
            .baseUrl(UrlUtils.baseIP())//设置基础请求IP
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(RetrofitService::class.java)
    }

    //拦截返回结果，未登录跳转到登录
    private fun interceptLogin(response: Response) {
        val responseBody = response.body
        val contentLength = responseBody!!.contentLength()
        if (contentLength != 0L) {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            val buffer = source.buffer.clone()
            val result = buffer.readString(Charset.defaultCharset())
            val fromJson = CommonConfig.fromJson(result, BaseResponse::class.java)
            if (fromJson.code == 401) {
                println(fromJson.msg)
                //获取当前的Activity并跳转
                UserDataUtils.jumpLogin(currentActivity)
            }

        }
    }

    lateinit var currentActivity: Activity
    override fun onActivityPaused(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {
        currentActivity = activity!!
    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityDestroyed(activity: Activity?) {

    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStopped(activity: Activity?) {

    }

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

    }
}