package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R

class TaskListAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_task_list){
    override fun convert(helper: BaseViewHolder?, item: String?) {
        when(item){
            "1" -> {
                helper?.setImageResource(R.id.iv_task_bg,R.mipmap.ic_task_1)
                helper?.setImageResource(R.id.iv_task_get,R.mipmap.ic_get_task_1)
            }
            "2" -> {
                helper?.setImageResource(R.id.iv_task_bg,R.mipmap.ic_task_2)
                helper?.setImageResource(R.id.iv_task_get,R.mipmap.ic_get_task_2)
            }
            "3" -> {
                helper?.setImageResource(R.id.iv_task_bg,R.mipmap.ic_task_3)
                helper?.setImageResource(R.id.iv_task_get,R.mipmap.ic_get_task_3)
            }
            "4" -> {
                helper?.setImageResource(R.id.iv_task_bg,R.mipmap.ic_task_4)
                helper?.setImageResource(R.id.iv_task_get,R.mipmap.ic_get_task_4)
            }
            "5" -> {
                helper?.setImageResource(R.id.iv_task_bg,R.mipmap.ic_task_5)
                helper?.setImageResource(R.id.iv_task_get,R.mipmap.ic_get_task_5)
            }
            "6" -> {
                helper?.setImageResource(R.id.iv_task_bg,R.mipmap.ic_task_6)
                helper?.setImageResource(R.id.iv_task_get,R.mipmap.ic_get_task_6)
            }
            "7" -> {
                helper?.setImageResource(R.id.iv_task_bg,R.mipmap.ic_task_7)
                helper?.setImageResource(R.id.iv_task_get,R.mipmap.ic_get_task_7)
            }
        }

        helper?.addOnClickListener(R.id.iv_task_get)
    }
}