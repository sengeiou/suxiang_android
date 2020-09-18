package com.sx.enjoy.modules.market

import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.view.dialog.NoticeDialog
import kotlinx.android.synthetic.main.activity_buy_in.*

class BuyInActivity : BaseActivity() {

    private lateinit var noticeDialog : NoticeDialog

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"我要买入")

    override fun getLayoutResource() = R.layout.activity_buy_in

    override fun initView() {
        noticeDialog = NoticeDialog(this)

        initEvent()
    }

    private fun initEvent(){
        tv_submit.setOnClickListener {
            noticeDialog.showNotice(3)
        }
    }

}