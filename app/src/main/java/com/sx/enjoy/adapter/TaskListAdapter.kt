package com.sx.enjoy.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.TaskListBean

class TaskListAdapter(var type:Int): BaseQuickAdapter<TaskListBean, BaseViewHolder>(R.layout.item_task_list){
    override fun convert(helper: BaseViewHolder?, item: TaskListBean?) {
        when(type){
            0 -> {
                helper?.setText(R.id.tv_task_name,item?.taskName)
                helper?.setText(R.id.tv_task_max,"最高奖励${item?.maxRich}米粒")
                helper?.setText(R.id.tv_task_walk,"有效步数${item?.effSteps}")
                helper?.setText(R.id.tv_day_rice,"单日奖励${item?.dayRich}")
                helper?.setText(R.id.tv_day_count,"时效${item?.taskEffTime}天")
                helper?.setVisible(R.id.tv_single_rice,true)
                helper?.setVisible(R.id.tv_rice_count,true)
                helper?.setText(R.id.tv_rice_count,item?.taskRich)
                helper?.setVisible(R.id.tv_rice_state,true)
                helper?.setText(R.id.tv_rice_state,"兑换所需米粒数")
                helper?.setVisible(R.id.tv_activity_count,true)
                helper?.setText(R.id.tv_activity_count,"活跃值${item?.activeValue}")
                helper?.getView<ImageView>(R.id.iv_task_get)?.visibility = View.VISIBLE
                helper?.getView<TextView>(R.id.tv_task_number)?.visibility = View.GONE
                helper?.getView<TextView>(R.id.tv_activity_count)?.visibility = View.VISIBLE
            }
            1 -> {
                helper?.setText(R.id.tv_task_name,item?.taskName)
                helper?.setText(R.id.tv_task_max,"最高奖励${item?.maxRich}米粒")
                helper?.setText(R.id.tv_task_walk,"有效步数${item?.effSteps}")
                helper?.setText(R.id.tv_day_rice,"单日奖励${item?.dayRich}")
                helper?.setText(R.id.tv_day_count,"时效${item?.taskEffTime}天")
                helper?.setVisible(R.id.tv_single_rice,false)
                helper?.setText(R.id.tv_rice_count,item?.exist)
                helper?.setVisible(R.id.tv_rice_count,false)
                helper?.setText(R.id.tv_rice_state,"已奖励")
                helper?.setVisible(R.id.tv_rice_state,false)
                helper?.setVisible(R.id.tv_activity_count,false)
                helper?.setText(R.id.tv_activity_count,"")
                helper?.getView<ImageView>(R.id.iv_task_get)?.visibility = View.GONE
                helper?.getView<TextView>(R.id.tv_task_number)?.visibility = View.GONE
                helper?.getView<TextView>(R.id.tv_activity_count)?.visibility = View.VISIBLE
            }
            2 -> {
                helper?.setText(R.id.tv_task_name,item?.taskName)
                helper?.setText(R.id.tv_task_max,"最高奖励${item?.maxRich}米粒")
                helper?.setText(R.id.tv_task_walk,"有效步数${item?.effSteps}")
                helper?.setText(R.id.tv_day_rice,"单日奖励${item?.dayRich}")
                helper?.setText(R.id.tv_day_count,"时效${item?.taskEffTime}天")
                helper?.setVisible(R.id.tv_single_rice,false)
                helper?.setText(R.id.tv_rice_count,item?.exist)
                helper?.setVisible(R.id.tv_rice_count,false)
                helper?.setText(R.id.tv_rice_state,"奖励")
                helper?.setVisible(R.id.tv_rice_state,false)
                helper?.setText(R.id.tv_activity_count,"")
                helper?.setVisible(R.id.tv_single_rice,false)
                helper?.setText(R.id.tv_task_number,"任务编号 ${item?.orderNo}")
                helper?.getView<ImageView>(R.id.iv_task_get)?.visibility = View.GONE
                helper?.getView<TextView>(R.id.tv_task_number)?.visibility = View.VISIBLE
                helper?.getView<TextView>(R.id.tv_activity_count)?.visibility = View.GONE
            }
            3 -> {
                when(item?.status){
                    0 -> {
                        helper?.setText(R.id.tv_task_name,item?.taskName)
                        helper?.setText(R.id.tv_task_max,"最高奖励${item?.maxRich}米粒")
                        helper?.setText(R.id.tv_task_walk,"有效步数${item?.effSteps}")
                        helper?.setText(R.id.tv_day_rice,"单日奖励${item?.dayRich}")
                        helper?.setText(R.id.tv_day_count,"时效${item?.taskEffTime}天")
                        helper?.setVisible(R.id.tv_single_rice,false)
                        helper?.setText(R.id.tv_rice_count,item?.exist)
                        helper?.setVisible(R.id.tv_rice_count,false)
                        helper?.setText(R.id.tv_rice_state,"已奖励")
                        helper?.setVisible(R.id.tv_rice_state,false)
                        helper?.setVisible(R.id.tv_activity_count,false)
                        helper?.setText(R.id.tv_activity_count,"")
                        helper?.getView<ImageView>(R.id.iv_task_get)?.visibility = View.GONE
                        helper?.getView<TextView>(R.id.tv_task_number)?.visibility = View.GONE
                        helper?.getView<TextView>(R.id.tv_activity_count)?.visibility = View.GONE
                    }
                    1 -> {
                        helper?.setText(R.id.tv_task_name,item?.taskName)
                        helper?.setText(R.id.tv_task_max,"最高奖励${item?.maxRich}米粒")
                        helper?.setText(R.id.tv_task_walk,"有效步数${item?.effSteps}")
                        helper?.setText(R.id.tv_day_rice,"单日奖励${item?.dayRich}")
                        helper?.setText(R.id.tv_day_count,"时效${item?.taskEffTime}天")
                        helper?.setVisible(R.id.tv_single_rice,false)
                        helper?.setText(R.id.tv_rice_count,item?.exist)
                        helper?.setVisible(R.id.tv_rice_count,false)
                        helper?.setText(R.id.tv_rice_state,"奖励")
                        helper?.setVisible(R.id.tv_rice_state,false)
                        helper?.setText(R.id.tv_activity_count,"")
                        helper?.setVisible(R.id.tv_single_rice,false)
                        helper?.setText(R.id.tv_task_number,"任务编号 ${item?.orderNo}")
                        helper?.getView<ImageView>(R.id.iv_task_get)?.visibility = View.GONE
                        helper?.getView<TextView>(R.id.tv_task_number)?.visibility = View.VISIBLE
                        helper?.getView<TextView>(R.id.tv_activity_count)?.visibility = View.GONE
                    }
                }
            }
        }

        
        when(item?.rank){
            1 -> {
                helper?.setImageResource(R.id.iv_task_bg,R.mipmap.ic_task_1)
                helper?.setImageResource(R.id.iv_task_get,R.mipmap.ic_get_task_1)
            }
            2 -> {
                helper?.setImageResource(R.id.iv_task_bg,R.mipmap.ic_task_2)
                helper?.setImageResource(R.id.iv_task_get,R.mipmap.ic_get_task_2)
            }
            3 -> {
                helper?.setImageResource(R.id.iv_task_bg,R.mipmap.ic_task_3)
                helper?.setImageResource(R.id.iv_task_get,R.mipmap.ic_get_task_3)
            }
            4 -> {
                helper?.setImageResource(R.id.iv_task_bg,R.mipmap.ic_task_4)
                helper?.setImageResource(R.id.iv_task_get,R.mipmap.ic_get_task_4)
            }
            5 -> {
                helper?.setImageResource(R.id.iv_task_bg,R.mipmap.ic_task_5)
                helper?.setImageResource(R.id.iv_task_get,R.mipmap.ic_get_task_5)
            }
            6 -> {
                helper?.setImageResource(R.id.iv_task_bg,R.mipmap.ic_task_6)
                helper?.setImageResource(R.id.iv_task_get,R.mipmap.ic_get_task_6)
            }
            7 -> {
                helper?.setImageResource(R.id.iv_task_bg,R.mipmap.ic_task_7)
                helper?.setImageResource(R.id.iv_task_get,R.mipmap.ic_get_task_7)
            }
        }

        helper?.addOnClickListener(R.id.iv_task_get)
    }
}