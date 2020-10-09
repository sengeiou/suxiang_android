package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.CommodityListBean
import com.sx.enjoy.utils.ImageLoaderUtil

class CommodityListAdapter: BaseQuickAdapter<CommodityListBean, BaseViewHolder>(R.layout.item_commodity_list){
    override fun convert(helper: BaseViewHolder?, item: CommodityListBean?) {
        ImageLoaderUtil().displayImage(mContext,item?.firstImage,helper?.getView(R.id.iv_commodity_image))
        helper?.setText(R.id.tv_commodity_name,item?.goodsName)
        helper?.setText(R.id.tv_commodity_amount,item?.amount)
    }
}