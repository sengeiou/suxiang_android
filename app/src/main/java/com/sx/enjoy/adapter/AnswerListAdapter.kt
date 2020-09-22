package com.sx.enjoy.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.OptionsList

class AnswerListAdapter: BaseQuickAdapter<OptionsList, BaseViewHolder>(R.layout.item_answer_list){
    override fun convert(helper: BaseViewHolder?, item: OptionsList) {
        helper?.setText(R.id.tv_answer_name,item.options)
        helper?.getView<TextView>(R.id.tv_answer_name)?.isSelected = item.isSelected
    }

    fun selectItem (position:Int){
        if(position == -1){
            data.forEach {
                it.isSelected = false
            }
            notifyDataSetChanged()
        }else{
            data.forEach {
                it.isSelected = false
            }
            data[position].isSelected = true
            notifyDataSetChanged()
        }
    }

    fun getSelectItem():String{
        var item = ""
        for(i in data.indices) {
            if(data[i].isSelected){
                item = data[i].options
                break
            }
        }
        return item
    }

}