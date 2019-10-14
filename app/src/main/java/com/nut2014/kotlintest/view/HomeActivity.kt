package com.nut2014.kotlintest.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.bumptech.glide.Glide
import com.jaeger.library.StatusBarUtil
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.base.MyApplication
import com.nut2014.kotlintest.network.runRxLambda
import com.nut2014.kotlintest.utils.PermissionUtils
import com.nut2014.kotlintest.utils.UpdateUtils
import com.nut2014.kotlintest.utils.UserDataUtils
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*

/**
 * 列表功能示例
 */
class HomeActivity : AppCompatActivity(), CoverFragment.OnFragmentInteractionListener {
    override fun onFragmentInteraction(imgUrl: String) {

    }

    private var bodyFragments: MutableList<Fragment>? = null
    private var titleList: MutableList<String>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.setTransparent(this)
        setContentView(R.layout.activity_home)

        toolbar_tb.title = "HOME"
        setSupportActionBar(toolbar_tb)
        init()
        setView()
        UpdateUtils(this).checkVersion()
        PermissionUtils.checkAll(this)

        bodyFragments = ArrayList()
        titleList = ArrayList()
        val myFragment1 = CoverFragment.newInstance(0)
        val myFragment2 = CoverFragment.newInstance(1)
        val myFragment3 = CoverFragment.newInstance(1)
        bodyFragments!!.add(myFragment1)
        //bodyFragments!!.add(myFragment2)
        bodyFragments!!.add(myFragment3)
        titleList!!.add("广场")
        //  titleList!!.add("关注")
        titleList!!.add("我的")
        val mAdapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return bodyFragments!![position]
            }

            override fun getCount(): Int {
                return bodyFragments!!.size
            }

            override fun getPageTitle(position: Int): CharSequence {
                return titleList!![position]
            }
        }
        vp_collect.adapter = mAdapter
        vp_collect.offscreenPageLimit = 2
        tab_collect.setupWithViewPager(vp_collect)

    }

    override fun onBackPressed() {
        //模拟HOME按键
        val intent = Intent(Intent.ACTION_MAIN)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.addCategory(Intent.CATEGORY_HOME)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        getUserInfo()
    }

    private fun getUserInfo() {
        val bgImg = UserDataUtils.getBgImg()
        if (bgImg.isNotEmpty()) {
            Glide.with(this).load(bgImg).into(top_iv)
        }
        if (UserDataUtils.getId() > 0) {
            runRxLambda(MyApplication.application().getService().getUser(UserDataUtils.getId()), {
                if (it.code == 1) {
                    UserDataUtils.saveUser(it.data)
                    setUserView()
                }
            }, {
                it?.printStackTrace()
            })
        }

    }

    private fun setUserView() {
        Glide.with(this).load(UserDataUtils.getBgImg()).into(top_iv)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val itemId = item!!.itemId
        if (itemId == android.R.id.home) {
            onBackPressed()
        } else if (itemId == R.id.user) {
            if (UserDataUtils.isLoginAndJump(this)) {
                startActivity(Intent(this@HomeActivity, SettingActivity::class.java))
            }

        }
        return super.onOptionsItemSelected(item)
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
            if (UserDataUtils.isLoginAndJump(this)) {
                startActivity(Intent(this@HomeActivity, AddCoverActivity::class.java))
            }
        }
        toolbar_tb.setOnClickListener {

        }


    }


}
