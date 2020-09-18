package com.sx.enjoy.modules.mine

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.likai.lib.base.BaseFragment
import com.sx.enjoy.R
import com.sx.enjoy.adapter.OrderListAdapter
import kotlinx.android.synthetic.main.fragment_public_list.*
import org.jetbrains.anko.startActivity

class OrderPagerFragment : BaseFragment(){

    private lateinit var mAdapter: OrderListAdapter

    override fun getLayoutResource() = R.layout.fragment_public_list


    override fun initView() {

        mAdapter = OrderListAdapter()
        rcy_public_list.layoutManager = LinearLayoutManager(activity)
        rcy_public_list.adapter = mAdapter


        initEvent()







        val mList = arrayListOf<String>()
        mList.add("")
        mList.add("")
        mList.add("")
        mAdapter.setNewData(mList)
    }

    private fun initEvent(){
        mAdapter.setOnItemClickListener { adapter, view, position ->
            activity?.startActivity<OrderDetailsActivity>()
        }
    }


    companion object {
        fun newInstance(type:Int): OrderPagerFragment {
            val fragment = OrderPagerFragment()
            val bundle = Bundle()
            bundle.putInt("type",type)
            fragment.arguments = bundle
            return fragment
        }
    }

}