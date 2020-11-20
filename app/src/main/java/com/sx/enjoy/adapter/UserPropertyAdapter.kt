package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.MarketListBean
import com.sx.enjoy.bean.NewMarketListBean
import com.sx.enjoy.bean.UserPropertyBean
import com.sx.enjoy.utils.ImageLoaderUtil

class UserPropertyAdapter(var type:Int): BaseQuickAdapter<UserPropertyBean, BaseViewHolder>(R.layout.item_user_property){
    override fun convert(helper: BaseViewHolder?, item: UserPropertyBean?) {
        helper?.setText(R.id.tv_type_name,item?.typeStr)
        helper?.setText(R.id.tv_create_time,item?.createTime)
        when(type){
            0 -> helper?.setText(R.id.tv_property_value,"+"+item?.contrib)
            1 -> helper?.setText(R.id.tv_property_value,"+"+item?.activity)
            2 -> helper?.setText(R.id.tv_property_value,"+"+item?.acqExp)
        }
    }
}