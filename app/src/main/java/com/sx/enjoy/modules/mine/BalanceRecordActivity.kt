package com.sx.enjoy.modules.mine

import androidx.recyclerview.widget.LinearLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.BalanceRecordAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_balance_record.*

class BalanceRecordActivity : BaseActivity() {

    private lateinit var mAdapter : BalanceRecordAdapter

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"提现记录")

    override fun getLayoutResource() = R.layout.activity_balance_record

    override fun initView() {

        mAdapter = BalanceRecordAdapter()
        rcy_balance_record.layoutManager = LinearLayoutManager(this)
        rcy_balance_record.adapter = mAdapter

        val mList = arrayListOf<String>()
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mAdapter.setNewData(mList)

    }
}