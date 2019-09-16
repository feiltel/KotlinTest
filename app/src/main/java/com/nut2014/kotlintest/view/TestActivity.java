package com.nut2014.kotlintest.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.liaoinstan.springview.widget.SpringView;
import com.nut2014.kotlintest.R;

public class TestActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
       Context context= TestActivity.this;
        final SpringView listSv = findViewById(R.id.list_sv);

        listSv.setListener(new SpringView.OnFreshListener() {
            @Override
            public void onRefresh() {
                listSv.onFinishFreshAndLoad();
            }

            @Override
            public void onLoadmore() {

            }
        });
    }

}
