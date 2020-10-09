package com.sx.enjoy.modules.store

import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.NewOrderBean
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_order_pay.*

class OrderPayActivity : BaseActivity() {

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"选择支付方式")

    override fun getLayoutResource() = R.layout.activity_order_pay

    override fun initView() {
        val order = intent.getSerializableExtra("order") as NewOrderBean
        tv_price.text = order.payPrice
    }
}