package com.nut2014.kotlintest.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import androidx.core.content.FileProvider;

import java.io.File;

/**
 * @author feiltel 2019/10/14 0014
 */
public class UriUtils {
    public static Uri File2Uri(Context context, File file, String providerStr) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // 7.0+以上版本
            return FileProvider.getUriForFile(context, providerStr, file);
        } else {
            return Uri.parse("file://" + file.toString());
        }
    }
}
