package com.nut2014.kotlintest.adapter

import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.entity.CoverInfo

/**
 * 订单查询列表适配器
 * Created by admin on 2016/1/13.
 */
class CoverInfoAdapter(layoutResId: Int, data: List<CoverInfo>?) :
    BaseQuickAdapter<CoverInfo, BaseViewHolder>(layoutResId, data) {


    override fun convert(holder: BaseViewHolder, orderInfo: CoverInfo) {

     /*   holder.setText(R.id.cover_tv, orderInfo.coverDes)
        holder.setText(R.id.tag_tv, orderInfo.tagName)
        holder.setText(R.id.username_tv, orderInfo.userName)
        holder.setText(R.id.like_tv, "${orderInfo.likeNumber}赞")
        Glide.with(mContext).load(orderInfo.coverImgPath).into(holder.getView(R.id.cover_iv))
        Glide.with(mContext).load(orderInfo.avatarPath).into(holder.getView(R.id.avatar_iv))*/
    }
}
