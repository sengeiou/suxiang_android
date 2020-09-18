package com.sx.enjoy.modules.mine

import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_balance.*
import org.jetbrains.anko.startActivity

class BalanceActivity : BaseActivity() {

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"余额")

    override fun getLayoutResource() = R.layout.activity_balance

    override fun initView() {

        initEvent()
    }

    private fun initEvent(){
        tv_withdrawal.setOnClickListener {
            startActivity<WithdrawalActivity>()
        }
        tv_balance_record.setOnClickListener {
            startActivity<BalanceRecordActivity>()
        }
    }

}