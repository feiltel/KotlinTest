package com.nut2014.kotlintest.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.nut2014.kotlintest.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    private ImagePicker imagePicker;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cover);
        imagePicker = new ImagePicker();
        imagePicker.setCropImage(false);
        imageView = findViewById(R.id.photo_iv);

        findViewById(R.id.link_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelect();

            }
        });
    }

    private void showSelect() {
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            @Override
            public void onPickImage(Uri imageUri) {
                File file = new File("");
                // Create a request body with file and image media type
                RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), file);
                // Create MultipartBody.Part using file request-body,file name and part name
                MultipartBody.Part part = MultipartBody.Part.createFormData("upload", file.getName(), fileReqBody);
                //Create request body with text description and text media type
                RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");

                Glide.with(TestActivity.this).load(imageUri).into(imageView);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(this, requestCode, resultCode, data);
    }

}
