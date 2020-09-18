package com.sx.enjoy.modules.task

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.likai.lib.base.BaseFragment
import com.sx.enjoy.R
import com.sx.enjoy.adapter.TaskListAdapter
import com.sx.enjoy.view.dialog.TaskGetDialog
import kotlinx.android.synthetic.main.fragment_task_child.*

class TaskChildFragment : BaseFragment(){

    private lateinit var mAdapter: TaskListAdapter
    private lateinit var taskDialog: TaskGetDialog

    override fun getLayoutResource() = R.layout.fragment_task_child


    override fun initView() {
        taskDialog = TaskGetDialog(activity!!)

        mAdapter = TaskListAdapter()
        rcy_task_child.layoutManager = LinearLayoutManager(activity)
        rcy_task_child.adapter = mAdapter

        initEvent()

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

    private fun initEvent(){
        mAdapter.setOnItemChildClickListener { _, view, position ->
            when(view.id){
                R.id.iv_task_get -> taskDialog.show()
            }
        }
    }


    companion object {
        fun newInstance(type:Int): TaskChildFragment {
            val fragment = TaskChildFragment()
            val bundle = Bundle()
            bundle.putInt("type",type)
            fragment.arguments = bundle
            return fragment
        }
    }

}