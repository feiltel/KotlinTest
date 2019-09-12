package com.nut2014.kotlintest.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.adapter.HomeListAdapter
import com.nut2014.kotlintest.base.BaseApplication
import com.nut2014.kotlintest.entity.Cover
import com.nut2014.kotlintest.network.runRxLambda
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.appcompat.v7.expandedMenuView
import org.jetbrains.anko.toast
import java.util.*

/**
 * 列表功能示例
 */
class HomeActivity : AppCompatActivity() {
    private var pageInt: Int = 1
    private lateinit var adapter: HomeListAdapter
    private lateinit var dataList: ArrayList<Cover>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        toolbar_tb.title = "HOME"
        setSupportActionBar(toolbar_tb)
        init()
        setView()
        getUserListData(pageInt)
    }


    /**
     * 初始化
     */
    private fun init() {
        dataList = ArrayList()
        adapter = HomeListAdapter(R.layout.list_item, dataList)
    }

    /**
     * 设置View
     */
    private fun setView() {
        toolbar_tb.setOnClickListener(View.OnClickListener {
            list_rv.smoothScrollToPosition(0)
            top_AppBarLayout.setExpanded(true)
        })
        list_rv.adapter = adapter

        list_rv.layoutManager = GridLayoutManager(this, 2)
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            toast("$position>>>>${dataList.get(position).coverDes}")
        }
        adapter.setOnLoadMoreListener({

            getUserListData(pageInt)
        }, list_rv)
    }

    /**
     * 获取数据
     */
    private fun getUserListData(pageNumber: Int = 1) {
        runRxLambda(BaseApplication.App().getService().getCovers(pageNumber), {
            if (it.code == 1) {
                if (it.data.isNotEmpty()){
                    Glide.with(this@HomeActivity).load(it.data[0].coverImgPath).into(top_iv)
                }

                adapter.addData(it.data)
                if (it.pageNum==it.pages){
                    adapter.loadMoreEnd()
                }else{
                    pageInt++
                    adapter.loadMoreComplete()
                }
            } else {
                adapter.loadMoreFail()
            }
        }, {
            it?.printStackTrace()
            adapter.loadMoreFail()
        })
    }
}
