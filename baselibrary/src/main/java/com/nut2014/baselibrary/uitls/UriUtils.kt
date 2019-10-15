package com.nut2014.baselibrary.uitls

import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider

import java.io.File

/**
 * @author feiltel 2019/10/14 0014
 */
object UriUtils {
    fun file2Uri(context: Context, file: File, providerStr: String): Uri {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            FileProvider.getUriForFile(context, providerStr, file)
        } else {
            Uri.parse("file://$file")
        }
    }
}
