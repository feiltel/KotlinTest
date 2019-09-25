package com.nut2014.kotlintest.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.liaoinstan.springview.widget.SpringView
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.adapter.AliHeader
import com.nut2014.kotlintest.adapter.HomeListAdapter
import com.nut2014.kotlintest.base.BaseApplication
import com.nut2014.kotlintest.entity.Cover
import com.nut2014.kotlintest.network.runRxLambda
import com.nut2014.kotlintest.utils.Contant
import com.nut2014.kotlintest.utils.UserDataUtils
import kotlinx.android.synthetic.main.fragment_cover.*
import org.jetbrains.anko.support.v4.toast
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
        init()
        setView()
        getUserListData(pageInt)
    }

    /**
     * 设置View
     */
    private fun setView() {

        list_rv.adapter = adapter
        adapter.openLoadAnimation()
        list_rv.layoutManager = StaggeredGridLayoutManager(2, 1)
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            startActivity(Intent(activity, InfoActivity::class.java))
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
            if (param1!! <= 0) BaseApplication.App().getService().getCovers(pageNumber) else BaseApplication.App().getService().getUserPage(
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
                if (it.code == 401) {
                    toast(it.msg)

                    startActivityForResult(Intent(activity, LoginActivity::class.java), Contant.loginRequstCode)
                }
                adapter.loadMoreFail()
            }
        }, {
            list_sv.onFinishFreshAndLoad()
            it?.printStackTrace()
            adapter.loadMoreFail()

        })
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: String) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Contant.loginRequstCode && resultCode == 1) {
            onButtonPressed("")
        }
    }


    /**
     * 初始化
     */
    private fun init() {
        dataList = ArrayList()
        adapter = HomeListAdapter(R.layout.list_item, dataList)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
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
