package com.nut2014.kotlintest.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.nut2014.baselibrary.uitls.ImageUtils
import com.nut2014.kotlintest.R
import com.nut2014.kotlintest.entity.Cover

/**
 * 订单查询列表适配器
 * Created by admin on 2016/1/13.
 */
class HomeListAdapter(layoutResId: Int, data: List<Cover>?, private var isUser: Boolean) :
    BaseQuickAdapter<Cover, BaseViewHolder>(layoutResId, data) {


    override fun convert(holder: BaseViewHolder, orderInfo: Cover) {
        holder.setGone(R.id.user_group, !isUser)
        holder.setText(R.id.cover_tv, orderInfo.coverDes)
        holder.setText(R.id.tag_tv, orderInfo.tagName)
        holder.setText(R.id.username_tv, orderInfo.userName)
        if (orderInfo.likeNumber > 0) {
            holder.setText(R.id.like_tv, "${orderInfo.likeNumber}")
        } else {
            holder.setText(R.id.like_tv, "")
        }

        val split = orderInfo.coverImgPath.split(",")
        if (split.isNotEmpty() && split[0].isNotEmpty()) {
            holder.setGone(R.id.cover_iv, true)
            ImageUtils.loadImg(mContext, split[0], holder.getView(R.id.cover_iv))
        } else {
            holder.setGone(R.id.cover_iv, false)
        }
        ImageUtils.loadCircleImg(mContext, orderInfo.avatarPath, holder.getView(R.id.avatar_iv))
    }
}
