package com.nut2014.kotlintest.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author feiltel 2019/10/14 0014
 */
public class FileUtils {

    public static String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static File savePicture(Bitmap bm, String fileName) {

        if (null == bm) {
            return null;
        }
        File folder = new File(rootPath + "/test");
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File myCaptureFile = new File(folder, fileName);
        try {
            if (!myCaptureFile.exists()) {
                myCaptureFile.createNewFile();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            //压缩保存到本地
            bm.compress(Bitmap.CompressFormat.JPEG, 90, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCaptureFile;
    }
}
