package com.nut2014.kotlintest.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.entity.User

/**
 * 订单查询列表适配器
 * Created by admin on 2016/1/13.
 */
class HomeListAdapter(layoutResId: Int, data: List<User>?) :
    BaseQuickAdapter<User, BaseViewHolder>(layoutResId, data) {


    override fun convert(holder: BaseViewHolder, orderInfo: User) {
        holder.setText(R.id.title_tv, orderInfo.userName)
        holder.setText(R.id.des_tv, orderInfo.passWord)
        holder.setText(R.id.pass_tv, orderInfo.realName)
    }
}
