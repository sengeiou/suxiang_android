package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.MarketListBean
import com.sx.enjoy.utils.ImageLoaderUtil

class MarketListAdapter: BaseQuickAdapter<MarketListBean, BaseViewHolder>(R.layout.item_market_list){
    override fun convert(helper: BaseViewHolder?, item: MarketListBean?) {
        ImageLoaderUtil().displayHeadImage(mContext,item?.userImg,helper?.getView(R.id.iv_user_head))
        helper?.setText(R.id.tv_user_name,item?.userName)
        helper?.setText(R.id.tv_rice_count,"数量 20${item?.richNum}")
        helper?.setText(R.id.tv_rice_amount,"¥ ${item?.amount}")
        helper?.setImageResource(R.id.iv_market_state,if(item?.type == 1) R.mipmap.ic_market_sell else R.mipmap.ic_market_buy)
    }
}