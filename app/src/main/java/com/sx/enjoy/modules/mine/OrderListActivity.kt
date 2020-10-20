package com.sx.enjoy.modules.mine

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
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
        when(intent.getIntExtra("type",C.ORDER_ALL)){
            C.ORDER_ALL -> vp_order_page.currentItem = 0
            C.ORDER_NO_PAY -> vp_order_page.currentItem = 1
            C.ORDER_NO_SEND -> vp_order_page.currentItem = 2
            C.ORDER_NO_RECEIVE -> vp_order_page.currentItem = 3
            C.ORDER_RECEIVE_OVER -> vp_order_page.currentItem = 4
        }
        fragments[vp_order_page.currentItem].setIsFirstShow(true)

        vp_order_page.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {}
            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}
            override fun onPageSelected(p0: Int) {
                setOrderPagerRefresh(p0)
            }
        })
    }

    private fun setOrderPagerRefresh(position:Int){
        fragments[position].getOrderList(isRefresh = true, isShowProgress = true)
    }

    override fun onResume() {
        super.onResume()
        setOrderPagerRefresh(vp_order_page.currentItem)
    }

}