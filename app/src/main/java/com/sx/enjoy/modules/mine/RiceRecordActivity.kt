package com.sx.enjoy.modules.mine

import android.support.v7.widget.LinearLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.RiceRecordAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_rice_record.*

class RiceRecordActivity : BaseActivity() {

    private lateinit var mAdapter : RiceRecordAdapter

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"米粒明细")

    override fun getLayoutResource() = R.layout.activity_rice_record

    override fun initView() {

        mAdapter = RiceRecordAdapter()
        rcy_rice_record.layoutManager = LinearLayoutManager(this)
        rcy_rice_record.adapter = mAdapter


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