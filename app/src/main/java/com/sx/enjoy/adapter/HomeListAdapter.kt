package com.sx.enjoy.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sx.enjoy.R
import com.sx.enjoy.bean.NewsListBean
import com.sx.enjoy.utils.ImageLoaderUtil

class HomeListAdapter: BaseQuickAdapter<NewsListBean, BaseViewHolder>(R.layout.item_home_list){
    override fun convert(helper: BaseViewHolder?, item: NewsListBean?) {
        helper?.setText(R.id.tv_new_title,item?.title)
        helper?.setText(R.id.tv_news_summery,item?.summary)
        ImageLoaderUtil().displayImage(mContext,item?.img,helper?.getView(R.id.iv_news_img))
    }
}