package com.nut2014.kotlintest.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.liaoinstan.springview.widget.SpringView
import com.nut2014.eventbuslib.FunctionManager
import com.nut2014.eventbuslib.FunctionNoParamNoResult
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.adapter.AliHeader
import com.nut2014.kotlintest.adapter.HomeListAdapter
import com.nut2014.kotlintest.base.MyApplication
import com.nut2014.kotlintest.entity.Cover
import com.nut2014.kotlintest.network.runRxLambda
import com.nut2014.kotlintest.utils.CacheUtils
import com.nut2014.kotlintest.utils.UserDataUtils
import kotlinx.android.synthetic.main.fragment_cover.*
import java.util.*


private const val ARG_PARAM1 = "listType"


class CoverFragment : Fragment() {


    private var pageInt: Int = 1
    private lateinit var adapter: HomeListAdapter
    private lateinit var dataList: ArrayList<Cover>


    private var param1: Int? = 0
    private var listener: OnFragmentInteractionListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getInt(ARG_PARAM1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        println("CoverFragment>>>onViewCreated")
        init()
        setView()
        if (param1 == 0) {
            val coverList = CacheUtils.getCoverList()
            if (coverList.isNotEmpty()) {
                adapter.addData(coverList)
            }
            getUserListData(pageInt)
        } else {
            if (UserDataUtils.isLogin()) {
                getUserListData(pageInt)
            }
        }
        FunctionManager.getInstance().addFunction(object : FunctionNoParamNoResult("loginCallback_cover") {
            override fun function() {
                getUserListData(pageInt)
            }
        })
    }

    /**
     * 设置View
     */
    private fun setView() {

        list_rv.adapter = adapter
        adapter.openLoadAnimation()
        list_rv.layoutManager = StaggeredGridLayoutManager(2, 1)
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            /*  val intent = Intent(activity, InfoActivity::class.java)
              intent.putExtra("cover", dataList[position].toJson())
              startActivity(intent)*/
            val args = Bundle()
            args.putString("cover", dataList[position].toJson())
            findNavController().navigate(R.id.action_homeFragment_to_infoFragment, args)
        }
        adapter.setOnLoadMoreListener({
            getUserListData(pageInt)
        }, list_rv)
        list_sv.header = AliHeader(activity, false)
        list_sv.setListener(object : SpringView.OnFreshListener {
            override fun onRefresh() {
                pageInt = 1
                getUserListData(pageInt)

            }

            override fun onLoadmore() {
            }
        })

    }

    /**
     * 获取数据
     */
    private fun getUserListData(pageNumber: Int = 1) {
        val userId = UserDataUtils.getId()

        val covers =
            if (param1!! <= 0) MyApplication.application().getService().getCovers(pageNumber) else MyApplication.application().getService().getUserPage(
                pageNumber,
                userId
            )

        runRxLambda(covers, {
            if (pageNumber == 1) {
                dataList.clear()
                adapter.notifyDataSetChanged()
            }
            list_sv.onFinishFreshAndLoad()
            if (it.code == 1) {

                adapter.addData(it.data)
                if (it.pageNum >= it.pages) {
                    adapter.loadMoreEnd()
                } else {
                    pageInt = it.pageNum + 1
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


    /**
     * 初始化
     */
    private fun init() {
        dataList = ArrayList()
        adapter = HomeListAdapter(R.layout.list_item, dataList, param1!! > 0)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        CacheUtils.saveCoverList(dataList)

    }

    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(imgUrl: String)
    }

    companion object {

        @JvmStatic
        fun newInstance(type: Int) =
            CoverFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, type)

                }
            }
    }
}
