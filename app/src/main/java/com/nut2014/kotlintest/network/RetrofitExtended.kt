package com.nut2014.kotlintest.network

import android.content.Context
import android.util.Log
import com.nut2014.baselibrary.uitls.DeviceUuidFactory
import com.nut2014.kotlintest.BuildConfig
import com.nut2014.kotlintest.base.MyApplication
import com.nut2014.kotlintest.entity.BasePageResponse
import com.nut2014.kotlintest.utils.UrlUtils
import com.nut2014.kotlintest.utils.UserDataUtils
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 用于添加扩展方法
 */
fun Any.deBug(msg: String) = Log.e("${this.javaClass.simpleName}------->", msg)

fun <T> runRxLambda(
    observable: Observable<T>,
    next: (T) -> Unit,
    error: (e: Throwable?) -> Unit,
    completed: () -> Unit = { Log.e("completed", "completed") }
) {
    observable.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object : Observer<T> {
            override fun onComplete() {
                completed()
            }

            override fun onError(e: Throwable?) {
                error(e)
            }

            override fun onNext(value: T) {
                next(value)
            }

            override fun onSubscribe(d: Disposable?) {}
        })
}

fun getUploadFileService(file: File): Observable<BasePageResponse<String>> {
    return MyApplication.application().getService().uploadImage(
        getFilePart(file),
        RequestBody.create("text/plain".toMediaTypeOrNull(), "image-type")
    )
}

private fun getFilePart(file: File): MultipartBody.Part {
    val fileReqBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
    // Create MultipartBody.Part using file request-body,file name and part name
    return MultipartBody.Part.createFormData("file", file.name, fileReqBody)
}


private fun initRetrofit(context: Context): RetrofitService {
    //配置网络请求超时时间
    val build = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
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
                .header("userId", UserDataUtils.getId().toString())
                .header("token", UserDataUtils.getToken())
                .header("deviceId", DeviceUuidFactory.getDeviceUuid(context).toString())
                .method(original.method, original.body)
                .build()
            val response = chain.proceed(request)
            //拦截相应结果
            //interceptLogin(response)
            return response

        }

    }
    //添加拦截器
    build.addInterceptor(logging)
    //初始化retrofitService
    return  Retrofit.Builder()
        .client(build.build())
        .baseUrl(UrlUtils.baseIP())//设置基础请求IP
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build().create(RetrofitService::class.java)

}