package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R

class AnswerListAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_answer_list){
    override fun convert(helper: BaseViewHolder?, item: String?) {
        helper?.setText(R.id.tv_answer_name,item)
    }

    fun setAnswerList(count:Int){
        data.clear()
        when(count){
            1 -> data.add("A")
            2 -> {
                data.add("A")
                data.add("B")
            }
            3 -> {
                data.add("A")
                data.add("B")
                data.add("C")
            }
            4 -> {
                data.add("A")
                data.add("B")
                data.add("C")
                data.add("D")
            }
            5 -> {
                data.add("A")
                data.add("B")
                data.add("C")
                data.add("D")
                data.add("E")
            }
        }
        notifyDataSetChanged()
    }

}