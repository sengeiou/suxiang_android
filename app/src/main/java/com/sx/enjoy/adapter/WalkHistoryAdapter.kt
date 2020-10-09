package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.WalkHistoryBean

class WalkHistoryAdapter: BaseQuickAdapter<WalkHistoryBean, BaseViewHolder>(R.layout.item_walk_history){
    override fun convert(helper: BaseViewHolder?, item: WalkHistoryBean?) {
        helper?.setText(R.id.tv_time,item?.createTime)
        helper?.setText(R.id.tv_walk_step,item?.minStep)
        helper?.setText(R.id.tv_rice_count,item?.riceGrains)
    }
}