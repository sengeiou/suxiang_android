package com.sx.enjoy.modules.mine

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.modules.market.BuyInActivity
import com.sx.enjoy.modules.market.SellOutActivity
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.android.synthetic.main.title_public_view.*
import org.jetbrains.anko.startActivityForResult

class TransactionActivity : BaseActivity() {

    private var fragments = arrayListOf<TransactionFragment>()
    private var pagerTitle = arrayListOf("进行中","已完成")

    private var type = 0

    override fun getTitleType(): PublicTitleData {
        return PublicTitleData (C.TITLE_RIGHT_TEXT,if(type == C.MARKET_ORDER_STATUS_BUY) "我购买的" else "我售卖的",if(type == C.MARKET_ORDER_STATUS_BUY) "发布买入" else "发布卖出",0,R.color.color_1A1A1A)
    }

    override fun getLayoutResource() = R.layout.activity_transaction

    override fun initView() {
        type = intent.getIntExtra("type",C.MARKET_ORDER_STATUS_BUY)

        initFragment()
        initEvent()
    }

    private fun initFragment(){
        fragments.clear()
        fragments.add(TransactionFragment.newInstance(type,0))
        fragments.add(TransactionFragment.newInstance(type,1))

        vp_transaction.adapter = object : FragmentPagerAdapter(supportFragmentManager){
            override fun getItem(position: Int): Fragment = fragments[position]
            override fun getCount(): Int = fragments.size
            override fun getPageTitle(position: Int): CharSequence? = pagerTitle[position]
        }
        tl_transaction.setupWithViewPager(vp_transaction)
    }

    private fun initEvent(){
        ll_public_right.setOnClickListener {
            if(type == 0){
                startActivityForResult<BuyInActivity>(3001)
            }else{
                startActivityForResult<SellOutActivity>(3002)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == RESULT_OK){
            fragments.forEach {
                it.getMyMarketOrderList(true)
            }
        }
    }

}