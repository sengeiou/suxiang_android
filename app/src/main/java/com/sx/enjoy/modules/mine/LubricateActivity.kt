package com.sx.enjoy.modules.mine

import android.support.v7.widget.LinearLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.LubricateListAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_lubricate.*

class LubricateActivity : BaseActivity() {

    private lateinit var mAdapter: LubricateListAdapter

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"附近加油")

    override fun getLayoutResource() = R.layout.activity_lubricate

    override fun initView() {

        mAdapter = LubricateListAdapter()
        rcy_lubricate_list.layoutManager = LinearLayoutManager(this)
        rcy_lubricate_list.adapter = mAdapter



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