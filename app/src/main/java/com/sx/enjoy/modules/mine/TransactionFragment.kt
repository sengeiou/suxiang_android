package com.sx.enjoy.modules.mine

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.likai.lib.base.BaseFragment
import com.sx.enjoy.R
import com.sx.enjoy.adapter.TransactionListAdapter
import kotlinx.android.synthetic.main.fragment_public_list.*

class TransactionFragment : BaseFragment(){

    private lateinit var mAdapter : TransactionListAdapter

    private var type = 0

    override fun getLayoutResource() = R.layout.fragment_public_list

    override fun initView() {
        type = arguments!!.getInt("type",0)

        mAdapter = TransactionListAdapter()
        rcy_public_list.layoutManager = LinearLayoutManager(activity)
        rcy_public_list.adapter = mAdapter

        initEvent()
    }

    private fun initEvent(){

    }


    companion object {
        fun newInstance(type:Int): TransactionFragment {
            val fragment = TransactionFragment()
            val bundle = Bundle()
            bundle.putInt("type",type)
            fragment.arguments = bundle
            return fragment
        }
    }

}