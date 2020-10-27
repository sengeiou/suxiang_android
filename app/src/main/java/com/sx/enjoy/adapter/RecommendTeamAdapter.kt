package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.TeamListBean
import com.sx.enjoy.utils.ImageLoaderUtil

class RecommendTeamAdapter: BaseQuickAdapter<TeamListBean, BaseViewHolder>(R.layout.item_recommend_team){
    override fun convert(helper: BaseViewHolder?, item: TeamListBean?) {
        ImageLoaderUtil().displayHeadImage(mContext,item?.userImg,helper?.getView(R.id.iv_user_head))
        helper?.setText(R.id.tv_team_name,item?.userName)
        helper?.setText(R.id.tv_member_name,item?.membershipLevelName)
        helper?.setText(R.id.tv_user_contribution,"贡献值 ${item?.userContrib}")
        helper?.setText(R.id.tv_user_activity,"活跃度 ${item?.userActivity}")
    }
}