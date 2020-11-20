package com.sx.enjoy.modules.market

import android.view.Gravity
import android.view.View
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.MarketSellSuccessEvent
import com.sx.enjoy.modules.mine.AuthenticationActivity
import com.sx.enjoy.modules.mine.TransactionDetailsActivity
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.ImageLoaderUtil
import com.sx.enjoy.view.dialog.NoticeDialog
import com.sx.enjoy.view.dialog.ReminderDialog
import kotlinx.android.synthetic.main.activity_sell_rice.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.litepal.LitePal

class SellRiceActivity : BaseActivity() ,SXContract.View{

    private lateinit var noticeDialog: NoticeDialog
    private lateinit var reminderDialog: ReminderDialog

    private lateinit var present: SXPresent

    private var errorCode = "0"

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"我要卖出")

    override fun getLayoutResource() = R.layout.activity_sell_rice

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        noticeDialog = NoticeDialog(this)
        reminderDialog = ReminderDialog(this)

        val type = intent.getIntExtra("type",C.MARKET_ORDER_STATUS_BUY)
        val marketId = intent.getStringExtra("marketId")
        val amount = intent.getStringExtra("amount")
        val buyNum = intent.getStringExtra("buyNum")
        val orderNo = intent.getStringExtra("orderNo")
        val richOrderNo = intent.getStringExtra("richOrderNo")

        val user = LitePal.findLast(UserBean::class.java)
        if(user.isAliPay){
            ll_zfb_number.visibility = View.VISIBLE
            ll_zfb_code.visibility = View.VISIBLE
            tv_ali_number.text = user.aliNumber
            ImageLoaderUtil().displayImage(this,user.payQrcode,iv_zfb_code)
        }
        if(user.isWxPay){
            ll_wx_code.visibility = View.VISIBLE
            ImageLoaderUtil().displayImage(this,user.wxQrcode,iv_wx_code)
        }

        tv_sell_rice.setOnClickListener {
            present.createMarketOrder(C.USER_ID,type.toString(),amount,buyNum,richOrderNo,orderNo,"0")
        }

        noticeDialog.setOnDismissListener {
            startActivity<TransactionDetailsActivity>(Pair("richOrderNo",richOrderNo),Pair("type",1))
            finish()
        }

        reminderDialog.setOnNoticeConfirmListener(object :ReminderDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                if(errorCode == "12"){
                    startActivity<AuthenticationActivity>()
                }
            }
        })

        present.getRichFee(C.USER_ID)
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.CREATEMARKETORDER -> {
                    noticeDialog.showNotice(6)
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
        string?.let {
            if(it.contains  ("-")){
                val sm = it.split("-")
                errorCode = sm[0]
                when(sm[0]){
                    "11","12" -> {
                        reminderDialog.showReminder(sm[1])
                    }
                    else -> {
                        toast(sm[1]).setGravity(Gravity.CENTER, 0, 0)
                    }
                }
            }else{
                toast(string).setGravity(Gravity.CENTER, 0, 0)
            }
        }
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(boolean){
            toast("请检查网络连接").setGravity(Gravity.CENTER, 0, 0)
        }
    }

}