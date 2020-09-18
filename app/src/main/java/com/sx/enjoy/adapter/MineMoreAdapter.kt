package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.MineMoreBean

class MineMoreAdapter: BaseQuickAdapter<MineMoreBean, BaseViewHolder>(R.layout.item_mine_more){
    override fun convert(helper: BaseViewHolder?, item: MineMoreBean) {
        helper?.setImageResource(R.id.iv_img,item.moduleImg)
        helper?.setText(R.id.tv_name,item.moduleName)
    }
}