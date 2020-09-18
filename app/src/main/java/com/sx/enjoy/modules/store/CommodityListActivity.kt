package com.sx.enjoy.modules.store

import android.support.v7.widget.GridLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.CommodityListAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_commodity_list.*
import org.jetbrains.anko.startActivity

class CommodityListActivity : BaseActivity() {

    private lateinit var mAdapter : CommodityListAdapter

    private var priceState = 0

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"产品列表")

    override fun getLayoutResource() = R.layout.activity_commodity_list

    override fun initView() {
        mAdapter = CommodityListAdapter()
        rcy_commodity_list.layoutManager = GridLayoutManager(this,2)
        rcy_commodity_list.adapter = mAdapter

        initEvent()


        val mList = arrayListOf<String>()
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mAdapter.setNewData(mList)
    }

    private fun initEvent(){
        ll_sale_price.setOnClickListener {
            tv_sale_price.setTextColor(resources.getColor(R.color.main_color))
            tv_sale_count.setTextColor(resources.getColor(R.color.color_3A3A3A))
            if(priceState == 1){
                priceState = 2
                iv_up_img.setImageResource(R.mipmap.ic_up_normal)
                iv_down_img.setImageResource(R.mipmap.ic_down_checked)
            }else{
                priceState = 1
                iv_up_img.setImageResource(R.mipmap.ic_up_checked)
                iv_down_img.setImageResource(R.mipmap.ic_down_normal)
            }
        }
        tv_sale_count.setOnClickListener {
            tv_sale_price.setTextColor(resources.getColor(R.color.color_3A3A3A))
            tv_sale_count.setTextColor(resources.getColor(R.color.main_color))
            priceState == 0
            iv_up_img.setImageResource(R.mipmap.ic_up_normal)
            iv_down_img.setImageResource(R.mipmap.ic_down_normal)
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            startActivity<CommodityActivity>()
        }
    }

}