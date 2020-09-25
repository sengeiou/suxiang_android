package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.StoreCategoryBean

class TypeMenuAdapter: BaseQuickAdapter<StoreCategoryBean, BaseViewHolder>(R.layout.item_type_menu){
    override fun convert(helper: BaseViewHolder?, item: StoreCategoryBean) {
        helper?.setText(R.id.tv_menu_name,item.cateName)
        helper?.setVisible(R.id.v_menu_left,item.isSelected)
        if(item.isSelected){
            helper?.setTextColor(R.id.tv_menu_name,mContext.resources.getColor(R.color.main_color))
            helper?.setBackgroundColor(R.id.ll_menu_content,mContext.resources.getColor(R.color.white))
        }else{
            helper?.setTextColor(R.id.tv_menu_name,mContext.resources.getColor(R.color.title_main_color))
            helper?.setBackgroundColor(R.id.ll_menu_content,mContext.resources.getColor(R.color.background))
        }
    }

    fun selectItem(position:Int){
        data.forEach {
            it.isSelected = false
        }
        data[position].isSelected = true
        notifyDataSetChanged()
    }

}