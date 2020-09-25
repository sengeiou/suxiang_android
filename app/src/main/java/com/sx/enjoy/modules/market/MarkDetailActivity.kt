package com.sx.enjoy.modules.market

import android.view.View
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.MarketListBean
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.UserStateChangeEvent
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.ImageLoaderUtil
import kotlinx.android.synthetic.main.activity_market_detail.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class MarkDetailActivity : BaseActivity() ,SXContract.View{

    private lateinit var present: SXPresent

    private var marketId = ""

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,if(intent.getIntExtra("type",1)==1) "卖出详情" else "买入详情")

    override fun getLayoutResource() = R.layout.activity_market_detail

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        marketId = intent.getStringExtra("marketId")
        present.getMarketDetails(marketId)


        initEvent()
    }

    private fun initEvent(){
        tv_confirm.setOnClickListener {

        }
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETMARKETDETAILS -> {
                    data?.let {
                        data as MarketListBean
                        ImageLoaderUtil().displayHeadImage(this,data.userImg,iv_user_head)
                        tv_user_name.text = data.userName
                        tv_market_time.text = data.createTime
                        tv_market_price.text = "¥${data.amountSum}"
                        tv_market_count.text = data.richNum
                        tv_zfb_account.text = data.aliNumber
                        tv_confirm.visibility = View.VISIBLE
                        tv_confirm.text = if(data.type == 1) "买入" else "卖出"
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

}