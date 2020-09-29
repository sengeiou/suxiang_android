package com.sx.enjoy.modules.market

import android.view.View
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.MarketListBean
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.MarketBuySuccessEvent
import com.sx.enjoy.event.UserStateChangeEvent
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.ImageLoaderUtil
import kotlinx.android.synthetic.main.activity_market_detail.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class MarkDetailActivity : BaseActivity() ,SXContract.View{

    private lateinit var present: SXPresent

    private var marketDetail:MarketListBean? = null

    private var type = 0
    private var marketId = ""

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,if(type==0) "买入详情" else "卖出详情")

    override fun getLayoutResource() = R.layout.activity_market_detail

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        EventBus.getDefault().register(this)

        type = intent.getIntExtra("type",0)

        marketId = intent.getStringExtra("marketId")
        present.getMarketDetails(marketId)

        ll_ali_number.visibility = if(type == 0) View.GONE else View.VISIBLE

        initEvent()
    }

    private fun initEvent(){
        tv_confirm.setOnClickListener {
            marketDetail?.let {
                if(type == 0){
                    startActivity<SellRiceActivity>(Pair("amount",it.amount), Pair("buyNum",it.richNum), Pair("orderNo",it.orderNo))
                }else{

                }
            }
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun marketRiceChange(event: MarketBuySuccessEvent){
        finish()
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETMARKETDETAILS -> {
                    data?.let {
                        data as MarketListBean
                        marketDetail = data
                        ImageLoaderUtil().displayHeadImage(this,data.userImg,iv_user_head)
                        tv_user_name.text = data.userName
                        tv_market_time.text = data.createTime
                        tv_market_price.text = "¥${data.amount}"
                        tv_market_count.text = data.richNum
                        tv_zfb_account.text = data.alipayNumber
                        tv_confirm.visibility = View.VISIBLE
                        tv_confirm.text = if(data.type == 0) "卖出" else "买入"
                    }
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        toast(string!!)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(boolean){
            toast("请检查网络连接")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}