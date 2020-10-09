package com.sx.enjoy.adapter

import android.util.Log
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.SpecChildListBean
import com.sx.enjoy.bean.SpecList
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout

class SpecChildListAdapter: BaseQuickAdapter<SpecList, BaseViewHolder>(R.layout.item_spec_child_view){
    override fun convert(helper: BaseViewHolder?, item: SpecList) {
        helper?.setText(R.id.tv_spec_name,item.paramName)
        val specAdapter = object : TagAdapter<SpecChildListBean>(item.specificationVoList) {
            override fun getView(parent: FlowLayout?, position: Int, t: SpecChildListBean): View {
                val tv = View.inflate(mContext, R.layout.item_spec_view, null) as TextView
                tv.text = t.paramName
                if(t.isSelected){
                    tv.setTextColor(mContext.resources.getColor(R.color.main_color))
                    tv.setBackgroundResource(R.drawable.bg_main_line_second_1)
                }else {
                    tv.setTextColor(mContext.resources.getColor(R.color.title_main_color))
                    tv.setBackgroundResource(R.drawable.bg_grey_full_1)
                }
                if(!t.isStock){
                    tv.setTextColor(mContext.resources.getColor(R.color.color_666666))
                    tv.setBackgroundResource(R.drawable.bg_grey_full_1)
                }
                return tv
            }
        }

        val fb = helper?.getView<TagFlowLayout>(R.id.tf_space_list)
        fb?.adapter = specAdapter

        fb?.setOnTagClickListener { view, position, parent ->
            for(i in item.specificationVoList.indices){
                if(i == position){
                    if(item.specificationVoList[i].isStock){
                        item.specificationVoList[position].isSelected = !item.specificationVoList[position].isSelected
                        mOnSpecSelectListener?.onSpecSelect()
                    }
                }else{
                    if(item.specificationVoList[i].isStock){
                        item.specificationVoList[i].isSelected = false
                    }
                }
            }
            specAdapter.notifyDataChanged()
            false
        }
    }

    interface OnSpecSelectListener{
        fun onSpecSelect()
    }

    private var mOnSpecSelectListener : OnSpecSelectListener? = null

    fun setOnSpecSelectListener(mOnSpecSelectListener : OnSpecSelectListener){
        this.mOnSpecSelectListener = mOnSpecSelectListener
    }

}