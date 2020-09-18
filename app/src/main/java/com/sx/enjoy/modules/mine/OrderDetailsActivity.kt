package com.sx.enjoy.modules.mine

import com.sx.enjoy.R
import com.sx.enjoy.adapter.OrderCommodityAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.view.NoScrollLinearLayoutManager
import kotlinx.android.synthetic.main.activity_order_details.*

class OrderDetailsActivity : BaseActivity() {

    private lateinit var mAdapter: OrderCommodityAdapter

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"订单详情")

    override fun getLayoutResource() = R.layout.activity_order_details

    override fun initView() {
        mAdapter = OrderCommodityAdapter()
        rcy_commodity_list.layoutManager = NoScrollLinearLayoutManager(this)
        rcy_commodity_list.adapter = mAdapter



        val mList = arrayListOf<String>()
        mList.add("")
        mList.add("")
        mAdapter.setNewData(mList)

    }
}