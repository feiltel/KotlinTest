package com.nut2014.kotlintest.network

import android.util.Log
import com.nut2014.kotlintest.base.MyApplication
import com.nut2014.kotlintest.entity.BasePageResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

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
