package com.nut2014.kotlintest.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.nut2014.kotlintest.R;
import com.nut2014.kotlintest.adapter.HomeListAdapter;
import com.nut2014.kotlintest.entity.User;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private HomeListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        recyclerView = findViewById(R.id.list_rv);
        final List<User> dataList = new ArrayList<>();
        for (int i=0;i<20;i++){
            dataList.add(new User("dlfl", ">>>", ">>"));
        }


      adapter = new HomeListAdapter(R.layout.list_item, dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                adapter.addData(dataList);
                adapter.loadMoreComplete();
            }
        },recyclerView);
    }
}
