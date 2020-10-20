package com.sx.enjoy.modules.mine

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_finance.*

class FinanceActivity : BaseActivity() {

    private var fragments = arrayListOf<Fragment>()
    private var pagerTitle = arrayListOf("收入","支出")

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"财务列表")

    override fun getLayoutResource() = R.layout.activity_finance

    override fun initView() {
        initFragment()
    }

    private fun initFragment(){
        fragments.clear()
        fragments.add(FinanceFragment.newInstance(0))
        fragments.add(FinanceFragment.newInstance(1))

        vp_finance.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
            override fun getPageTitle(position: Int): CharSequence? = pagerTitle[position]
        }
        tl_finance.setupWithViewPager(vp_finance)
    }

}