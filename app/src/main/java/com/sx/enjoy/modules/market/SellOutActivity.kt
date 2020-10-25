package com.sx.enjoy.modules.market

import android.view.Gravity
import android.view.View
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.MarketSellSuccessEvent
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.view.dialog.NoticeDialog
import kotlinx.android.synthetic.main.activity_sell_out.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class SellOutActivity : BaseActivity() , SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var noticeDialog : NoticeDialog

    private var isSend = false

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"我要卖出")

    override fun getLayoutResource() = R.layout.activity_sell_out

    override fun initView() {
        noticeDialog = NoticeDialog(this)

        present = SXPresent(this)

        initEvent()

        present.getRichFee(C.USER_ID)
    }


    private fun initEvent(){
        tv_submit.setOnClickListener {
            if(et_sell_count.text.isEmpty()){
                toast("请输入卖出数量").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(et_sell_price.text.isEmpty()){
                toast("请输入买入单价").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(et_zfb_number.text.isEmpty()){
                toast("请输入支付宝账号").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(!isSend){
                isSend = true
                present.publishMarketInfo(C.USER_ID,C.MARKET_ORDER_STATUS_SELL.toString(),et_sell_price.text.toString(),et_sell_count.text.toString(),et_zfb_number.text.toString())
            }
        }
        noticeDialog.setOnDismissListener {
            finish()
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.PUBLISHMARKETINFO -> {
                    isSend = false
                    noticeDialog.showNotice(3)
                    EventBus.getDefault().post(MarketSellSuccessEvent(1))
                }
                SXContract.GETRICHFEE -> {
                    data?.let {
                        data as String
                        ll_rice_fee.visibility = View.VISIBLE
                        tv_rice_fee.text = "*交易手续费$data%"
                    }
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        isSend = false
        toast(string!!).setGravity(Gravity.CENTER, 0, 0)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        isSend = false
        if(boolean){
            toast("请检查网络连接").setGravity(Gravity.CENTER, 0, 0)
        }
    }

}