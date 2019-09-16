package com.nut2014.kotlintest.adapter;

import com.chad.library.adapter.base.loadmore.LoadMoreView;

public class MyLoadMoreView extends LoadMoreView {
    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected int getLoadingViewId() {
        return 0;
    }

    @Override
    protected int getLoadFailViewId() {
        return 0;
    }

    @Override
    protected int getLoadEndViewId() {
        return 0;
    }
}
