package com.sx.enjoy.modules.market

import android.support.v7.widget.LinearLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.IncomeAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_income.*
import kotlinx.android.synthetic.main.activity_income.swipe_refresh_layout
import kotlinx.android.synthetic.main.fragment_market.*

class IncomeActivity : BaseActivity() {

    private lateinit var mAdapter: IncomeAdapter

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"收入明细")

    override fun getLayoutResource() = R.layout.activity_income

    override fun initView() {

        mAdapter = IncomeAdapter()
        rcy_income_list.layoutManager = LinearLayoutManager(this)
        rcy_income_list.adapter = mAdapter


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