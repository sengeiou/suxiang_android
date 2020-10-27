package com.sx.enjoy.adapter

import android.view.View
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.StoreList
import com.sx.enjoy.modules.store.CommodityActivity
import com.sx.enjoy.utils.ImageLoaderUtil
import kotlinx.android.synthetic.main.item_order_commodity.view.*
import org.jetbrains.anko.startActivity

class OrderCommodityAdapter: BaseQuickAdapter<StoreList, BaseViewHolder>(R.layout.item_order_store){
    override fun convert(helper: BaseViewHolder?, item: StoreList) {
        helper?.setText(R.id.tv_store_name,item.storeName)

        val contentView = helper?.getView<LinearLayout>(R.id.ll_order_content)
        contentView?.removeAllViews()
        item.orderVoList.forEach{ ct ->
            val cView = View.inflate(mContext,R.layout.item_order_commodity,null)
            ImageLoaderUtil().displayImage(mContext,ct.image,cView.findViewById(R.id.iv_commodity_image))
            cView.tv_commodity_name.text = ct.goodsName
            cView.tv_commodity_amount.text = "¥${ct.goodsAmount}"
            cView.tv_spec_name.text = ct.constitute
            cView.tv_commodity_count.text = "x${ct.purchaseNum}"
            cView.ll_commodity_content.setOnClickListener {
                mContext.startActivity<CommodityActivity>(Pair("commodityId",ct.goodsId))
            }
            contentView?.addView(cView)
        }
    }
}