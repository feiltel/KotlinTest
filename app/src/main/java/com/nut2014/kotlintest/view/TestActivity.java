package com.nut2014.kotlintest.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.nut2014.kotlintest.R;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {


    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cover);

        imageView = findViewById(R.id.photo_iv);

        findViewById(R.id.link_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelect();

            }
        });
        Banner banne = null;
        List<String> paths = new ArrayList<>();
        banne.setImages(paths);
    }

    private void showSelect() {

    }
}
