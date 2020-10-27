package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.RiceRecordListBean

class RiceRecordAdapter: BaseQuickAdapter<RiceRecordListBean, BaseViewHolder>(R.layout.item_rice_record){

    override fun convert(helper: BaseViewHolder?, item: RiceRecordListBean) {
        helper?.setText(R.id.tv_rice_type,item.typeStr)
        helper?.setText(R.id.tv_rice_time,item.createTime)
        helper?.setText(R.id.tv_rice_income,if(item.isIncome == 0) "+${item.rich}" else "-${item.rich}")
        helper?.setText(R.id.tv_rice_fee,if(item.type != 1) "" else "手续费:${item.richFee}")
    }
}