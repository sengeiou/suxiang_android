package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.MarketTransactionListBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.utils.ImageLoaderUtil

class TransactionListAdapter(): BaseQuickAdapter<MarketTransactionListBean, BaseViewHolder>(R.layout.item_transaction_list){
    override fun convert(helper: BaseViewHolder?, item: MarketTransactionListBean) {
        helper?.setText(R.id.tv_rice_count,"数量 ${item.buyNum}")
        helper?.setText(R.id.tv_order_status,item.statusStr)
        ImageLoaderUtil().displayHeadImage(mContext,item.releaseUserImg,helper?.getView(R.id.iv_user_head))
        helper?.setText(R.id.tv_user_name,item.releaseUserName)

        helper?.setText(R.id.tv_rice_amount,"ZGCT单价: ${item.amount}")
        helper?.setText(R.id.tv_us_amount,"USDT单价: ${item.usdt}")
        helper?.setImageResource(R.id.iv_market_state,if(item.type == 1) R.mipmap.ic_market_sell else R.mipmap.ic_market_buy)
        helper?.setVisible(R.id.iv_pay_zfb,item.isAliPay == 1)
        helper?.setVisible(R.id.iv_pay_wx,item.isWxPay == 1)

    }
}