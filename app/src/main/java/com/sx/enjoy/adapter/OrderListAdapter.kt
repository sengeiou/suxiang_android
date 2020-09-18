package com.sx.enjoy.adapter

import android.view.View
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R

class OrderListAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_order_view){
    override fun convert(helper: BaseViewHolder?, item: String?) {

        val contentView = helper?.getView<LinearLayout>(R.id.ll_order_content)
        contentView?.removeAllViews()
        for(i in 0..1){
            val cView  = View.inflate(mContext,R.layout.item_order_commodity,null)
            contentView?.addView(cView)
        }

    }
}