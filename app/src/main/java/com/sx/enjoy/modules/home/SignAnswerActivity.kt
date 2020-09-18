package com.sx.enjoy.modules.home

import android.support.v7.widget.GridLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.AnswerListAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_sign_answer.*

class SignAnswerActivity : BaseActivity() {

    private lateinit var mAdapter : AnswerListAdapter

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"签到答题")

    override fun getLayoutResource() = R.layout.activity_sign_answer

    override fun initView() {

        mAdapter = AnswerListAdapter()
        rcy_answer_list.layoutManager = GridLayoutManager(this,3)
        rcy_answer_list.adapter = mAdapter



        mAdapter.setAnswerList(5)
    }
}