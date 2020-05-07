package com.nut2014.kotlintest.view


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.fragment.findNavController
import com.nut2014.baselibrary.uitls.ImageUtils
import com.nut2014.eventbuslib.FunctionManager
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.base.MyApplication
import com.nut2014.kotlintest.network.runRxLambda
import com.nut2014.kotlintest.utils.PermissionUtils
import com.nut2014.kotlintest.utils.UserDataUtils
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*
import com.nut2014.eventbuslib.FunctionNoParamNoResult as FunctionNoParamNoResult1


class HomeFragment : BaseFragment() {

    private var isInit = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private var bodyFragments: MutableList<Fragment>? = null
    private var titleList: MutableList<String>? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar_tb.title = "HOME"
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar_tb)
        init()
        setView()

        PermissionUtils.checkAll(requireActivity())

        bodyFragments = ArrayList()
        titleList = ArrayList()
        val myFragment1 = CoverFragment.newInstance(0)
        val myFragment3 = CoverFragment.newInstance(1)
        bodyFragments!!.add(myFragment1)
        //bodyFragments!!.add(myFragment2)
        bodyFragments!!.add(myFragment3)
        titleList!!.add("广场")
        //  titleList!!.add("关注")
        titleList!!.add("我的")
        val mAdapter = object : FragmentPagerAdapter(childFragmentManager, 0) {
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
        getUserInfo()
        FunctionManager.getInstance().addFunction(object : FunctionNoParamNoResult1("loginCallback_home") {
            override fun function() {
                getUserInfo()
            }
        })


    }

    override fun onDestroyView() {
        super.onDestroyView()

    }


    private fun getUserInfo() {
        val bgImg = UserDataUtils.getBgImg()
        if (bgImg.isNotEmpty()) {
            ImageUtils.loadImg(requireContext(), bgImg, top_iv)
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
        ImageUtils.loadImg(requireContext(), UserDataUtils.getBgImg(), top_iv)
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

            if (UserDataUtils.isLoginAndJump(requireActivity())) {
                findNavController().navigate(R.id.action_homeFragment_to_addCoverFragment)
            }
        }
        toolbar_tb.setOnClickListener {

        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId = item!!.itemId
        if (itemId == android.R.id.home) {
            findNavController().navigateUp()
        } else if (itemId == R.id.user) {
            if (UserDataUtils.isLoginAndJump(requireActivity())) {
                findNavController().navigate(R.id.action_homeFragment_to_settingFragment)
            }

        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)
    }

}
