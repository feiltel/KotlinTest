package com.nut2014.baselibrary.uitls

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions

/**
 * @author feiltel 2019/10/15 0015
 */
object ImageUtils {
    fun loadImg(mContext: Context, path: String, imageView: ImageView) {
        if (path.isNotEmpty()) {
            Glide.with(mContext).load(path)
                .into(imageView)
        }

    }

    fun loadCircleImg(mContext: Context, path: String, imageView: ImageView) {
        if (path.isNotEmpty()) {
            Glide.with(mContext).load(path)
                .apply(RequestOptions.bitmapTransform(CircleCrop()))
                .into(imageView)
        }
    }

}
