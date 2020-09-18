package com.sx.enjoy.modules.store

import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sx.enjoy.R
import com.sx.enjoy.adapter.ShopCartAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.view.SwipeItemLayout
import kotlinx.android.synthetic.main.activity_shop_cart.*
import kotlinx.android.synthetic.main.title_public_view.*

class ShopCartActivity : BaseActivity() {

    private lateinit var mAdapter: ShopCartAdapter

    private var isEdit = false

    override fun getTitleType() = PublicTitleData (C.TITLE_RIGHT_TEXT,"购物车","编辑",0,R.color.color_3A3A3A)

    override fun getLayoutResource() = R.layout.activity_shop_cart

    override fun initView() {

        mAdapter = ShopCartAdapter()
        rcy_shop_cart.layoutManager = LinearLayoutManager(this)
        rcy_shop_cart.adapter = mAdapter
        rcy_shop_cart.addOnItemTouchListener(object: SwipeItemLayout.OnSwipeItemTouchListener(this){})

        initEvent()



        val mList = arrayListOf<String>()
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mAdapter.setNewData(mList)

    }

    private fun initEvent(){
        tv_public_right.setOnClickListener {
            isEdit = !isEdit
            if(isEdit){
                tv_public_right.text = "完成"
                tv_shop_confirm.text = "删除"
                tv_total_money.visibility = View.INVISIBLE
            }else{
                tv_public_right.text = "编辑"
                tv_shop_confirm.text = "结算"
                tv_total_money.visibility = View.VISIBLE
            }
        }
    }

}