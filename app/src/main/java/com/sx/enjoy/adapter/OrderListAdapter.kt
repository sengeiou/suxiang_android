package com.sx.enjoy.adapter

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.OrderListBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.utils.ImageLoaderUtil
import kotlinx.android.synthetic.main.item_order_commodity.view.*

class OrderListAdapter: BaseQuickAdapter<OrderListBean, BaseViewHolder>(R.layout.item_order_view){
    override fun convert(helper: BaseViewHolder?, item: OrderListBean) {
        helper?.setText(R.id.tv_order_no,item.orderNo)
        helper?.setText(R.id.tv_order_number,item.totalNumber)
        helper?.setText(R.id.tv_order_price,item.total)
        helper?.setText(R.id.tv_order_status,item.statusStr)

        val contentView = helper?.getView<LinearLayout>(R.id.ll_order_content)
        contentView?.removeAllViews()
        item.orderVoList.forEach{
            val cView  = View.inflate(mContext,R.layout.item_order_commodity,null)
            ImageLoaderUtil().displayImage(mContext,it.image,cView.findViewById(R.id.iv_commodity_image))
            cView.tv_commodity_name.text = it.goodsName
            cView.tv_commodity_amount.text = "¥${it.goodsAmount}"
            cView.tv_spec_name.text = it.constitute
            cView.tv_commodity_count.text = "x${it.purchaseNum}"
            contentView?.addView(cView)
        }

        when(item.status){
            C.ORDER_NO_PAY -> {
                helper?.setText(R.id.tv_order_button_1,"取消订单")
                helper?.setText(R.id.tv_order_button_2,"去支付")

                helper?.getView<LinearLayout>(R.id.ll_order_bottom)?.visibility = View.VISIBLE
                helper?.getView<TextView>(R.id.tv_order_button_1)?.visibility = View.VISIBLE
                helper?.getView<TextView>(R.id.tv_order_button_2)?.visibility = View.VISIBLE
            }
            C.ORDER_NO_SEND -> {
                helper?.setText(R.id.tv_order_button_1,"提醒发货")
                helper?.setText(R.id.tv_order_button_2,"")

                helper?.getView<LinearLayout>(R.id.ll_order_bottom)?.visibility = View.VISIBLE
                helper?.getView<TextView>(R.id.tv_order_button_1)?.visibility = View.VISIBLE
                helper?.getView<TextView>(R.id.tv_order_button_2)?.visibility = View.GONE
            }
            C.ORDER_NO_RECEIVE -> {
                helper?.setText(R.id.tv_order_button_1,"")
                helper?.setText(R.id.tv_order_button_2,"确认收货")

                helper?.getView<LinearLayout>(R.id.ll_order_bottom)?.visibility = View.VISIBLE
                helper?.getView<TextView>(R.id.tv_order_button_1)?.visibility = View.GONE
                helper?.getView<TextView>(R.id.tv_order_button_2)?.visibility = View.VISIBLE
            }
            C.ORDER_RECEIVE_OVER -> {
                helper?.setText(R.id.tv_order_button_1,"")
                helper?.setText(R.id.tv_order_button_2,"")

                helper?.getView<LinearLayout>(R.id.ll_order_bottom)?.visibility = View.GONE
                helper?.getView<TextView>(R.id.tv_order_button_1)?.visibility = View.GONE
                helper?.getView<TextView>(R.id.tv_order_button_2)?.visibility = View.GONE
            }
            C.ORDER_ORDER_CANCEL -> {
                helper?.setText(R.id.tv_order_button_1,"删除订单")
                helper?.setText(R.id.tv_order_button_2,"")

                helper?.getView<LinearLayout>(R.id.ll_order_bottom)?.visibility = View.VISIBLE
                helper?.getView<TextView>(R.id.tv_order_button_1)?.visibility = View.VISIBLE
                helper?.getView<TextView>(R.id.tv_order_button_2)?.visibility = View.GONE
            }
        }

        helper?.addOnClickListener(R.id.tv_order_button_1)
        helper?.addOnClickListener(R.id.tv_order_button_2)
    }
}