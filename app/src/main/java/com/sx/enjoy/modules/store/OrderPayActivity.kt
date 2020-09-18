package com.sx.enjoy.modules.store

import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C

class OrderPayActivity : BaseActivity() {

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"选择支付方式")

    override fun getLayoutResource() = R.layout.activity_order_pay

    override fun initView() {

    }
}