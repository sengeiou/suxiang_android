package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.OrderSendBean
import com.sx.enjoy.utils.ImageLoaderUtil

class OrderConfirmAdapter: BaseQuickAdapter<OrderSendBean, BaseViewHolder>(R.layout.item_order_confirm){
    override fun convert(helper: BaseViewHolder?, item: OrderSendBean?) {
        ImageLoaderUtil().displayImage(mContext,item?.image,helper?.getView(R.id.iv_shop_image))
        helper?.setText(R.id.tv_shop_name,item?.name)
        helper?.setText(R.id.tv_shop_price,"¥${item?.price}")
        helper?.setText(R.id.tv_spec_name,item?.specName)
        helper?.setText(R.id.tv_shop_count,"x${item?.number}")
    }
}