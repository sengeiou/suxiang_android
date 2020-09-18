package com.sx.enjoy.modules.mine

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.likai.lib.base.BaseFragment
import com.sx.enjoy.R
import com.sx.enjoy.adapter.FinanceListAdapter
import kotlinx.android.synthetic.main.fragment_public_list.*

class FinanceFragment : BaseFragment(){

    private lateinit var mAdapter : FinanceListAdapter

    override fun getLayoutResource() = R.layout.fragment_public_list


    override fun initView() {

        mAdapter = FinanceListAdapter()
        rcy_public_list.layoutManager = LinearLayoutManager(activity)
        rcy_public_list.adapter = mAdapter


        val mList = arrayListOf<String>()
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mAdapter.setNewData(mList)

    }

    private fun initEvent(){

    }


    companion object {
        fun newInstance(type:Int): FinanceFragment {
            val fragment = FinanceFragment()
            val bundle = Bundle()
            bundle.putInt("type",type)
            fragment.arguments = bundle
            return fragment
        }
    }

}