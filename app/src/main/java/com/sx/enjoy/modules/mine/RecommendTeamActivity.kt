package com.sx.enjoy.modules.mine

import android.support.v7.widget.LinearLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.RecommendTeamAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_recommend_team.*

class RecommendTeamActivity : BaseActivity() {

    private lateinit var mAdapter: RecommendTeamAdapter

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"推荐团队")

    override fun getLayoutResource() = R.layout.activity_recommend_team

    override fun initView() {

        mAdapter = RecommendTeamAdapter()
        rcy_recommend_team.layoutManager = LinearLayoutManager(this)
        rcy_recommend_team.adapter = mAdapter



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