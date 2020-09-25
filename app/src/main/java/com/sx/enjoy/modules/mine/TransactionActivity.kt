package com.sx.enjoy.modules.mine

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_transaction.*

class TransactionActivity : BaseActivity() {

    private var fragments = arrayListOf<Fragment>()
    private var pagerTitle = arrayListOf("进行中","已完成")

    private var type = 1

    override fun getTitleType(): PublicTitleData {
        return PublicTitleData (C.TITLE_RIGHT_TEXT,if(type == 1) "我购买的" else "我售卖的",if(type == 1) "发布买入" else "发布卖出",R.color.color_1A1A1A)
    }

    override fun getLayoutResource() = R.layout.activity_transaction

    override fun initView() {
        type = intent.getIntExtra("type",1)

        initFragment()
    }

    private fun initFragment(){
        fragments.clear()
        fragments.add(TransactionFragment.newInstance(0))
        fragments.add(TransactionFragment.newInstance(1))

        vp_transaction.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
            override fun getPageTitle(position: Int): CharSequence? = pagerTitle[position]
        }
        tl_transaction.setupWithViewPager(vp_transaction)
    }

}