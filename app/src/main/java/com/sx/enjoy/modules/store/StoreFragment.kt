package com.sx.enjoy.modules.store

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.likai.lib.base.BaseFragment
import com.sx.enjoy.R
import com.sx.enjoy.adapter.TypeContentAdapter
import com.sx.enjoy.adapter.TypeMenuAdapter
import com.sx.enjoy.bean.TestTypeMenuBean
import kotlinx.android.synthetic.main.fragment_store.*
import org.jetbrains.anko.startActivity

class StoreFragment : BaseFragment(){

    private lateinit var menuAdapter: TypeMenuAdapter
    private lateinit var contentAdapter: TypeContentAdapter

    private lateinit var headTitleView : View

    override fun getLayoutResource() = R.layout.fragment_store


    override fun initView() {
        ImmersionBar.with(activity!!).statusBarDarkFont(true).titleBar(tb_store_title).init()

        menuAdapter = TypeMenuAdapter()
        rcy_menu_list.layoutManager = LinearLayoutManager(activity)
        rcy_menu_list.adapter = menuAdapter

        contentAdapter = TypeContentAdapter()
        rcy_type_list.layoutManager = GridLayoutManager(activity,3)
        rcy_type_list.adapter = contentAdapter

        headTitleView = View.inflate(activity,R.layout.header_type_title,null)
        contentAdapter.addHeaderView(headTitleView)

        initEvent()





        val menuList = arrayListOf<TestTypeMenuBean>()
        menuList.add(TestTypeMenuBean("裤子专区",true))
        menuList.add(TestTypeMenuBean("外套专区",false))
        menuList.add(TestTypeMenuBean("鞋子专区",false))
        menuList.add(TestTypeMenuBean("优惠专区",false))
        menuList.add(TestTypeMenuBean("沙发专区",false))
        menuList.add(TestTypeMenuBean("会员专区",false))
        menuAdapter.setNewData(menuList)


        val mList = arrayListOf<String>()
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        mList.add("")
        contentAdapter.setNewData(mList)

    }

    private fun initEvent(){
        menuAdapter.setOnItemClickListener { adapter, view, position ->
            menuAdapter.selectItem(position)
        }
        contentAdapter.setOnItemClickListener { adapter, view, position ->
            activity?.startActivity<CommodityListActivity>()
        }
    }


    companion object {
        fun newInstance(): StoreFragment {
            val fragment = StoreFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

}