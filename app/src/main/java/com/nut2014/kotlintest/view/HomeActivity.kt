package com.nut2014.kotlintest.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.liaoinstan.springview.widget.SpringView
import com.liaoinstan.springview.widget.SpringView.OnFreshListener
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.adapter.AliFooter
import com.nut2014.kotlintest.adapter.AliHeader
import com.nut2014.kotlintest.adapter.HomeListAdapter
import com.nut2014.kotlintest.base.BaseApplication
import com.nut2014.kotlintest.entity.Cover
import com.nut2014.kotlintest.network.runRxLambda
import kotlinx.android.synthetic.main.activity_home.*
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
        add_fab.setOnClickListener{
            startActivity(Intent(this@HomeActivity, AddCoverActivity::class.java))
        }
        toolbar_tb.setOnClickListener {
            list_rv.scrollToPosition(0)
            top_AppBarLayout.setExpanded(true)
        }
        list_rv.adapter = adapter
        adapter.openLoadAnimation()
        list_rv.layoutManager = GridLayoutManager(this@HomeActivity, 2)
      //  list_rv.layoutManager = StaggeredGridLayoutManager(2, 1)
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            toast("$position>>>>${dataList[position].coverDes}")
            Glide.with(this@HomeActivity).load(dataList[position].coverImgPath).into(top_iv)
        }
        adapter.setOnLoadMoreListener({
            getUserListData(pageInt)
        }, list_rv)
        list_sv.header=AliHeader(this@HomeActivity,false)
     //   list_sv.footer=AliFooter(this@HomeActivity,false)
        list_sv.setListener(object : OnFreshListener {
            override fun onRefresh() {
                pageInt=1
                getUserListData(pageInt)

            }

            override fun onLoadmore() {
               // getUserListData(pageInt)
            }
        })

    }

    /**
     * 获取数据
     */
    private fun getUserListData(pageNumber: Int = 1) {
        runRxLambda(BaseApplication.App().getService().getCovers(pageNumber), {
            if(pageNumber==1){
                dataList.clear()
                adapter.notifyDataSetChanged()
            }
            list_sv.onFinishFreshAndLoad()
            if (it.code == 1) {
                if (it.data.isNotEmpty()) {
                    Glide.with(this@HomeActivity).load(it.data[0].coverImgPath).into(top_iv)
                }

                adapter.addData(it.data)
                if (it.pageNum == it.pages) {
                    adapter.loadMoreEnd()
                } else {
                    pageInt++
                    adapter.loadMoreComplete()
                }
            } else {
                adapter.loadMoreFail()
            }
        }, {
            list_sv.onFinishFreshAndLoad()
            it?.printStackTrace()
            adapter.loadMoreFail()

        })
    }
}
