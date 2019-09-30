package com.nut2014.kotlintest.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下载文件
 */
public class DownloadFile extends AsyncTask<String, String, String> {

    private KProgressHUD progressDialog;


    private boolean isDownloaded;

    public void setCanDownload(boolean canDownload) {
        this.canDownload = canDownload;
    }

    private boolean canDownload = true;
    private Context context;
    private DownloadCallBack downloadCallBack;

    public DownloadFile setShowProgress(boolean showProgress) {
        isShowProgress = showProgress;
        return this;
    }

    private boolean isShowProgress = true;


    public DownloadFile(Context context, DownloadCallBack downloadCallBack) {
        canDownload = true;
        this.context = context;
        this.downloadCallBack = downloadCallBack;
    }

    public interface DownloadCallBack {
        void success(String path);

        void error(String msg);

        void progress(int progress);
    }

    /**
     * Before starting background thread
     * Show Progress Bar Dialog
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (isShowProgress) {
            this.progressDialog = new KProgressHUD(context)
                    .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                    .setCancellable(false)
                    .setMaxProgress(100)
                    .show();
        }

    }

    /**
     * Downloading file in background thread
     */
    @Override
    protected String doInBackground(String... f_url) {

        int count;
        try {
            URL url = new URL(f_url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();
            // getting file length
            int lengthOfFile = connection.getContentLength();


            // input stream to read file - with 8k buffer
            InputStream input = new BufferedInputStream(url.openStream(), 8192);

            String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());

            //Extract file name from URL
            String fileName = f_url[0].substring(f_url[0].lastIndexOf('/') + 1, f_url[0].length());

            //Append timestamp to file name
            fileName = timestamp + "_" + fileName;

            //External directory path to save file
            String folder = Environment.getExternalStorageDirectory() + File.separator + "nut2014/";

            //Create androiddeft folder if it does not exist
            File directory = new File(folder);

            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Output stream to write file
            OutputStream output = new FileOutputStream(folder + fileName);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1 && canDownload) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
                publishProgress("" + (int) ((total * 100) / lengthOfFile));
                Log.d("Progress", "Progress: " + (int) ((total * 100) / lengthOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
            return folder + fileName;

        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
            assert downloadCallBack != null;
            downloadCallBack.error(e.getMessage());
        }

        return "Something went wrong";
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        if (progressDialog != null) {
            progressDialog.setProgress(Integer.parseInt(progress[0]));
        }
        assert downloadCallBack != null;
        downloadCallBack.progress(Integer.parseInt(progress[0]));

    }


    @Override
    protected void onPostExecute(String message) {
        // dismiss the dialog after the file was downloaded
        if (progressDialog != null) {
            this.progressDialog.dismiss();
        }
        assert downloadCallBack != null;
        downloadCallBack.success(message);
    }
}
