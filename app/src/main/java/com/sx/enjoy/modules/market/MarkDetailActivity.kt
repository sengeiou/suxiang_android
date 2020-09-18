package com.sx.enjoy.modules.market

import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_market_detail.*

class MarkDetailActivity : BaseActivity() {


    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"卖出详情")

    override fun getLayoutResource() = R.layout.activity_market_detail

    override fun initView() {

        initEvent()
    }

    private fun initEvent(){
        tv_confirm.setOnClickListener {

        }
    }

}