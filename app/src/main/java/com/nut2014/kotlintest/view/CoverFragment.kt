package com.nut2014.kotlintest.view

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.support.v4.toast
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CoverFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CoverFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CoverFragment : Fragment() {
    private var pageInt: Int = 1
    private lateinit var adapter: HomeListAdapter
    private lateinit var dataList: ArrayList<Cover>


    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
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
        // list_rv.layoutManager = GridLayoutManager(this@HomeActivity, 2)
        list_rv.layoutManager = StaggeredGridLayoutManager(2, 1)
        adapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            // toast("$position>>>>${dataList[position].coverDes}")
            // Glide.with(this@HomeActivity).load(dataList[position].coverImgPath).into(top_iv)
            startActivity(Intent(activity, InfoActivity::class.java))
        }
        adapter.setOnLoadMoreListener({
            getUserListData(pageInt)
        }, list_rv)
        list_sv.header = AliHeader(activity, false)
        //   list_sv.footer=AliFooter(this@HomeActivity,false)
        list_sv.setListener(object : SpringView.OnFreshListener {
            override fun onRefresh() {
                pageInt = 1
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
                    startActivity(Intent(activity, LoginActivity::class.java))
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
    fun onButtonPressed(uri: Uri) {
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
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CoverFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
