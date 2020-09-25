package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.StoreCategoryBean
import com.sx.enjoy.bean.StoreCategoryChildBean
import com.sx.enjoy.utils.ImageLoaderUtil

class TypeContentAdapter: BaseQuickAdapter<StoreCategoryChildBean, BaseViewHolder>(R.layout.item_type_content){
    override fun convert(helper: BaseViewHolder?, item: StoreCategoryChildBean?) {
        ImageLoaderUtil().displayImage(mContext,item?.cateImage,helper?.getView(R.id.iv_store_type))
        helper?.setText(R.id.tv_store_type,item?.cateName)
    }
}