package com.nut2014.kotlintest.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.adapter.HomeListAdapter
import com.nut2014.kotlintest.base.BaseApplication
import com.nut2014.kotlintest.entity.User
import com.nut2014.kotlintest.network.runRxLambda
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.toast
import java.util.*

/**
 * 列表功能示例
 */
class HomeActivity : AppCompatActivity() {
    private lateinit var adapter: HomeListAdapter
    private lateinit var dataList: ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        toolbar_tb.title = "HOME"
        setSupportActionBar(toolbar_tb)
        init()
        setView()
        getUserListData("初始化", "chushihua")
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
        list_rv.adapter = adapter
        list_rv.layoutManager = LinearLayoutManager(this)
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            toast("$position>>>>${dataList.get(position).userName}")
        }
        adapter.setOnLoadMoreListener({
            getUserListData("张三", "9999999999")
        }, list_rv)
    }

    /**
     * 获取数据
     */
    private fun getUserListData(userName: String = "", passWord: String = "") {
        runRxLambda(BaseApplication.App().getService().getUsers(userName, passWord), {
            if (it.code == 1) {
                adapter.addData(it.data)
                adapter.loadMoreComplete()
            } else {
                adapter.loadMoreFail()
            }
        }, {
            it?.printStackTrace()
            adapter.loadMoreFail()
        })
    }
}
