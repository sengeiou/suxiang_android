package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.MarketTransactionListBean
import com.sx.enjoy.utils.ImageLoaderUtil

class TransactionListAdapter: BaseQuickAdapter<MarketTransactionListBean, BaseViewHolder>(R.layout.item_transaction_list){
    override fun convert(helper: BaseViewHolder?, item: MarketTransactionListBean) {
        ImageLoaderUtil().displayHeadImage(mContext,item.headImage,helper?.getView(R.id.iv_user_head))
        helper?.setText(R.id.tv_user_name,item.userName)
        helper?.setText(R.id.tv_rice_count,"数量 ${item.buyNum}")
        helper?.setText(R.id.tv_rice_amount,"¥ ${item.buyAmountSum}")
        helper?.setImageResource(R.id.iv_market_state,if(item.type == 1) R.mipmap.ic_market_sell else R.mipmap.ic_market_buy)
    }
}