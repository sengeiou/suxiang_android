package com.sx.enjoy.modules.home

import android.support.v7.widget.LinearLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.WalkHistoryAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_walk_history.*

class WalkHistoryActivity : BaseActivity() {

    private lateinit var mAdapter: WalkHistoryAdapter

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"历史步数")

    override fun getLayoutResource() = R.layout.activity_walk_history

    override fun initView() {

        mAdapter = WalkHistoryAdapter()
        rcy_walk_history.layoutManager = LinearLayoutManager(this)
        rcy_walk_history.adapter = mAdapter


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