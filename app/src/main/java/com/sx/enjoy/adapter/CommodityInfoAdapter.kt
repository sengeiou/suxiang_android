package com.sx.enjoy.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.likai.lib.commonutils.ScreenUtils
import com.sx.enjoy.R
import com.sx.enjoy.utils.ImageLoaderUtil


class CommodityInfoAdapter: BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_image_show){
    override fun convert(helper: BaseViewHolder, item: String?) {
        val showImage = helper.getView<ImageView>(R.id.iv_show)
        ImageLoaderUtil().displayCommodityInfoImage(mContext,item,showImage)
    }
}