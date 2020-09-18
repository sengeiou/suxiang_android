package com.sx.enjoy.modules.mine

import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C

class FeedbackActivity : BaseActivity() {

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"问题反馈")

    override fun getLayoutResource() = R.layout.activity_feedback

    override fun initView() {

    }
}