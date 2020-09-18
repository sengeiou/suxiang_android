package com.sx.enjoy

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.likai.lib.base.BaseFragment
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.modules.home.HomeFragment
import com.sx.enjoy.modules.market.MarketFragment
import com.sx.enjoy.modules.mine.MineFragment
import com.sx.enjoy.modules.store.StoreFragment
import com.sx.enjoy.modules.task.TaskFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.home_bottom_tab_button.*

class MainActivity :BaseActivity() {

    private var fragments = arrayListOf<BaseFragment>()

    override fun getTitleType() = PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource() = R.layout.activity_main

    override fun initView() {

        initFragment()

    }

    private fun initFragment(){
        fragments.clear()
        fragments.add(HomeFragment.newInstance())
        fragments.add(TaskFragment.newInstance())
        fragments.add(MarketFragment.newInstance())
        fragments.add(StoreFragment.newInstance())
        fragments.add(MineFragment.newInstance())

        vp_home.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
        }
        vp_home.offscreenPageLimit = 4
        ll_home.setOnClickListener {
            select(0)
        }
        ll_task.setOnClickListener {
            select(1)
        }
        ll_market.setOnClickListener {
            select(2)
        }
        ll_shop.setOnClickListener {
            select(3)
        }
        ll_mine.setOnClickListener {
            select(4)
        }
        select(0)
    }

    private fun select(position:Int) {
        when (position) {
            0 -> {
                img_home.isSelected = true
                img_task.isSelected = false
                img_market.isSelected = false
                img_shop.isSelected = false
                img_mine.isSelected = false
                tv_home.isSelected = true
                tv_task.isSelected = false
                tv_market.isSelected = false
                tv_shop.isSelected = false
                tv_mine.isSelected = false
                vp_home.setCurrentItem(0, false)
            }
            1 -> {
                img_home.isSelected = false
                img_task.isSelected = true
                img_market.isSelected = false
                img_shop.isSelected = false
                img_mine.isSelected = false
                tv_home.isSelected = false
                tv_task.isSelected = true
                tv_market.isSelected = false
                tv_shop.isSelected = false
                tv_mine.isSelected = false
                vp_home.setCurrentItem(1, false)
            }
            2 -> {
                img_home.isSelected = false
                img_task.isSelected = false
                img_market.isSelected = true
                img_shop.isSelected = false
                img_mine.isSelected = false
                tv_home.isSelected = false
                tv_task.isSelected = false
                tv_market.isSelected = true
                tv_shop.isSelected = false
                tv_mine.isSelected = false
                vp_home.setCurrentItem(2, false)
            }
            3 -> {
                img_home.isSelected = false
                img_task.isSelected = false
                img_market.isSelected = false
                img_shop.isSelected = true
                img_mine.isSelected = false
                tv_home.isSelected = false
                tv_task.isSelected = false
                tv_market.isSelected = false
                tv_shop.isSelected = true
                tv_mine.isSelected = false
                vp_home.setCurrentItem(3, false)
            }
            4 -> {
                img_home.isSelected = false
                img_task.isSelected = false
                img_market.isSelected = false
                img_shop.isSelected = false
                img_mine.isSelected = true
                tv_home.isSelected = false
                tv_task.isSelected = false
                tv_market.isSelected = false
                tv_shop.isSelected = false
                tv_mine.isSelected = true
                vp_home.setCurrentItem(4, false)
            }
        }
    }

}