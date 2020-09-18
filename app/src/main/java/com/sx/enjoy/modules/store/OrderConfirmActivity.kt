package com.sx.enjoy.modules.store

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sx.enjoy.R
import com.sx.enjoy.adapter.OrderConfirmAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_order_confirm.*
import kotlinx.android.synthetic.main.header_order_confirm.view.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult

class OrderConfirmActivity : BaseActivity() {

    private lateinit var mAdapter: OrderConfirmAdapter
    private lateinit var headView: View
    private lateinit var footView: View

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"确认订单")

    override fun getLayoutResource() = R.layout.activity_order_confirm

    override fun initView() {

        mAdapter = OrderConfirmAdapter()
        rcy_order_confirm.layoutManager = LinearLayoutManager(this)
        rcy_order_confirm.adapter = mAdapter

        headView = View.inflate(this,R.layout.header_order_confirm,null)
        footView = View.inflate(this,R.layout.footer_order_confirm,null)


        initEvent()


        val mList = arrayListOf<String>()
        mList.add("")
        mList.add("")
        mList.add("")
        mAdapter.setNewData(mList)
        mAdapter.addHeaderView(headView)
        mAdapter.addFooterView(footView)

    }

    private fun initEvent(){
        headView.ll_order_address.setOnClickListener {
            startActivityForResult<AddressEditActivity>(1002,Pair("type",0))
        }
        tv_order_confirm.setOnClickListener {
            startActivity<OrderPayActivity>()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK&&requestCode == 1002){
            headView.ll_address_view.visibility = View.VISIBLE
            headView.tv_add_address.visibility = View.GONE
        }
    }

}