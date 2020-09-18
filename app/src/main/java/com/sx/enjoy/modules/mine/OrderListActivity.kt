package com.sx.enjoy.modules.mine

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_order_list.*

class OrderListActivity : BaseActivity() {

    private var pagerTitle = arrayListOf("全部","待付款","待发货","待收货","已收货")
    private var fragments = arrayListOf<OrderPagerFragment>()

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"我的订单")

    override fun getLayoutResource() = R.layout.activity_order_list

    override fun initView() {

        initFragment()

    }

    private fun initFragment(){
        fragments.clear()
        fragments.add(OrderPagerFragment.newInstance(C.ORDER_ALL))
        fragments.add(OrderPagerFragment.newInstance(C.ORDER_NO_PAY))
        fragments.add(OrderPagerFragment.newInstance(C.ORDER_NO_SEND))
        fragments.add(OrderPagerFragment.newInstance(C.ORDER_NO_RECEIVE))
        fragments.add(OrderPagerFragment.newInstance(C.ORDER_RECEIVE_OVER))

        vp_order_page.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
            override fun getPageTitle(position: Int): CharSequence? = pagerTitle[position]
        }
        tl_order_type.setupWithViewPager(vp_order_page)
        vp_order_page.currentItem = intent.getIntExtra("type",0)
        //fragments[vp_order_page.currentItem].setIsFirstShow(true)

        vp_order_page.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
            override fun onPageSelected(p0: Int) {
                //setOrderPagerRefresh(p0)
            }
        })

    }

}