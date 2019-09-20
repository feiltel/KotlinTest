package com.nut2014.kotlintest.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.liaoinstan.springview.widget.SpringView.OnFreshListener
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.adapter.AliHeader
import com.nut2014.kotlintest.adapter.HomeListAdapter
import com.nut2014.kotlintest.base.BaseApplication
import com.nut2014.kotlintest.entity.Cover
import com.nut2014.kotlintest.network.runRxLambda
import com.nut2014.kotlintest.utils.PermissionUtils
import com.nut2014.kotlintest.utils.UpdateUtils
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.toast
import java.util.*

/**
 * 列表功能示例
 */
class HomeActivity : AppCompatActivity(),CoverFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        toolbar_tb.title = "HOME"
        setSupportActionBar(toolbar_tb)
        init()
        setView()
        UpdateUtils(this).checkVersion()
        PermissionUtils.checkAll(this)
    }


    /**
     * 初始化
     */
    private fun init() {

    }

    /**
     * 设置View
     */
    private fun setView() {
        add_fab.setOnClickListener {
            startActivity(Intent(this@HomeActivity, TestActivity::class.java))
        }
        toolbar_tb.setOnClickListener {
            list_rv.scrollToPosition(0)
            top_AppBarLayout.setExpanded(true)
        }



    }


}
