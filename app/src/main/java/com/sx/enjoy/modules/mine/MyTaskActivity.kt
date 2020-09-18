package com.sx.enjoy.modules.mine

import android.support.v7.widget.LinearLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.TaskListAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_my_task.*

class MyTaskActivity : BaseActivity() {

    private lateinit var mAdapter: TaskListAdapter

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"我的任务")

    override fun getLayoutResource() = R.layout.activity_my_task

    override fun initView() {

        mAdapter = TaskListAdapter()
        rcy_my_task.layoutManager = LinearLayoutManager(this)
        rcy_my_task.adapter = mAdapter


        val mList = arrayListOf<String>()
        mList.add("1")
        mList.add("2")
        mList.add("3")
        mList.add("4")
        mList.add("5")
        mList.add("6")
        mList.add("7")
        mAdapter.setNewData(mList)

    }
}