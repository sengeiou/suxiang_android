package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.MarketTransactionListBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.utils.ImageLoaderUtil

class TransactionListAdapter(var type:Int): BaseQuickAdapter<MarketTransactionListBean, BaseViewHolder>(R.layout.item_transaction_list){
    override fun convert(helper: BaseViewHolder?, item: MarketTransactionListBean) {
        helper?.setText(R.id.tv_rice_count,"数量 ${item.buyNum}")
        helper?.setText(R.id.tv_rice_amount,"¥ ${item.amount}")
        ImageLoaderUtil().displayHeadImage(mContext,item.releaseUserImg,helper?.getView(R.id.iv_user_head))
        helper?.setText(R.id.tv_user_name,item.releaseUserName)
        if(type == C.MARKET_ORDER_STATUS_BUY){
            if(item.type == 0){
                helper?.setImageResource(R.id.iv_market_state,R.mipmap.ic_market_buy)
            }else{
                helper?.setImageResource(R.id.iv_market_state,R.mipmap.ic_market_sell)
            }
        }else{
            if(item.type == 0){
                helper?.setImageResource(R.id.iv_market_state,R.mipmap.ic_market_buy)
            }else{
                helper?.setImageResource(R.id.iv_market_state,R.mipmap.ic_market_sell)
            }
        }
    }
}