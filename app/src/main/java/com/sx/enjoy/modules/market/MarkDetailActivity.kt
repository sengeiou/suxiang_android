package com.sx.enjoy.modules.market

import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.view.View
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.MarketListBean
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.MarketSellSuccessEvent
import com.sx.enjoy.modules.mine.AuthenticationActivity
import com.sx.enjoy.modules.mine.PayMethodSetActivity
import com.sx.enjoy.modules.mine.TransactionDetailsActivity
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.ImageLoaderUtil
import com.sx.enjoy.view.dialog.NormalSetDialog
import com.sx.enjoy.view.dialog.NoticeDialog
import com.sx.enjoy.view.dialog.ReminderDialog
import com.sx.enjoy.view.dialog.SingleImageShowDialog
import kotlinx.android.synthetic.main.activity_market_detail.*
import kotlinx.android.synthetic.main.empty_public_network.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.litepal.LitePal

class MarkDetailActivity : BaseActivity() ,SXContract.View{

    private lateinit var noticeDialog: NoticeDialog
    private lateinit var reminderDialog: ReminderDialog
    private lateinit var normalSetDialog: NormalSetDialog
    private lateinit var singleImageDialog : SingleImageShowDialog

    private lateinit var present: SXPresent

    private var marketDetail:MarketListBean? = null

    private var type = 0
    private var isSend = false
    private var marketId = ""
    private var errorCode = "0"
    private var isWXPay = false
    private var isALIPay = false

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,if(type==0) "买入详情" else "卖出详情")

    override fun getLayoutResource() = R.layout.activity_market_detail

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        EventBus.getDefault().register(this)

        val user = LitePal.findLast(UserBean::class.java)
        if(user!=null){
            isALIPay = user.isAliPay
            isWXPay = user.isWxPay
        }

        noticeDialog = NoticeDialog(this)
        reminderDialog = ReminderDialog(this)
        normalSetDialog = NormalSetDialog(this)
        singleImageDialog = SingleImageShowDialog(this)

        type = intent.getIntExtra("type",0)

        marketId = intent.getStringExtra("marketId")
        present.getMarketDetails(marketId)

        initEvent()
    }

    private fun initEvent(){
        tv_confirm.setOnClickListener {
            marketDetail?.let {
                if(type == C.MARKET_ORDER_STATUS_BUY){
                    if(!isWXPay&&!isALIPay){
                        normalSetDialog.showNotice(1)
                        return@setOnClickListener
                    }
                    var pay = 0
                    if(it.isAliPay == 1&&it.isWxPay == 0&&!isALIPay){
                        pay = 1
                    }else if(it.isAliPay == 0&&it.isWxPay == 1&&!isWXPay){
                        pay = 2
                    }
                    when(pay){
                        0 -> startActivity<SellRiceActivity>(Pair("type",type),Pair("marketId",marketId),Pair("amount",it.amount), Pair("buyNum",it.richNum), Pair("orderNo",it.orderNo),
                            Pair("richOrderNo",it.richOrderNo))
                        1 -> toast("该订单仅支持支付宝支付,请上传支付宝收款账户").setGravity(Gravity.CENTER, 0, 0)
                        2 -> toast("该订单仅支持微信支付,请上传微信收款账户").setGravity(Gravity.CENTER, 0, 0)
                    }

                }else{
                    if(!isSend){
                        isSend = true
                        present.createMarketOrder(C.USER_ID,type.toString(),it.amount,it.richNum,it.richOrderNo,it.orderNo,"0")
                    }
                }
            }
        }

        noticeDialog.setOnDismissListener {
            startActivity<TransactionDetailsActivity>(Pair("richOrderNo",marketDetail?.richOrderNo),Pair("type",0))
            finish()
        }

        iv_network_error.setOnClickListener {
            present.getMarketDetails(marketId)
        }
        reminderDialog.setOnNoticeConfirmListener(object :ReminderDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                if(errorCode == "12"){
                    startActivity<AuthenticationActivity>()
                }
            }
        })
        normalSetDialog.setOnNoticeConfirmListener(object :NormalSetDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                startActivityForResult<PayMethodSetActivity>(4002)
            }
        })

        ll_ali_code.setOnClickListener {
            singleImageDialog.showImage(marketDetail?.aliQrcode)
        }
        ll_wx_code.setOnClickListener {
            singleImageDialog.showImage(marketDetail?.wxQrcode)
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun marketRiceChange(event: MarketSellSuccessEvent){
        if(event.state == 1){
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK&&requestCode == 4002){
            val user = LitePal.findLast(UserBean::class.java)
            if(user!=null){
                isALIPay = user.isAliPay
                isWXPay = user.isWxPay
            }
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETMARKETDETAILS -> {
                    data?.let {
                        data as MarketListBean
                        em_network_view.visibility = View.GONE
                        marketDetail = data
                        ImageLoaderUtil().displayHeadImage(this,data.userImg,iv_user_head)
                        tv_user_name.text = data.userName
                        tv_market_time.text = data.createTime
                        tv_market_price.text = "¥${data.amount}"
                        tv_market_count.text = data.richNum
                        tv_total_price.text = "¥${data.amountSum}"

                        iv_zfb_pay.visibility = if(data.isAliPay == 1) View.VISIBLE else View.GONE
                        iv_wx_pay.visibility = if(data.isWxPay == 1) View.VISIBLE else View.GONE

                        if(data.type == 0){
                            ll_pay_method.visibility = View.VISIBLE
                            ll_ali_number.visibility = View.GONE
                            v_ali_line.visibility = View.GONE
                        }else{
                            ll_pay_method.visibility = View.GONE
                            if(data.isAliPay == 1){
                                ll_ali_number.visibility = View.VISIBLE
                                ll_ali_code.visibility = View.VISIBLE
                                v_ali_line.visibility = View.VISIBLE

                                tv_zfb_account.text = data.aliNumber
                            }
                            if(data.isWxPay == 1){
                                v_wx_line.visibility = View.VISIBLE
                                ll_wx_code.visibility = View.VISIBLE
                            }
                        }

                        tv_confirm.visibility = if(C.USER_ID.isEmpty()||data.userId == C.USER_ID) View.GONE else View.VISIBLE
                        tv_confirm.text = if(data.type == 0) "卖出" else "买入"
                    }
                }
                SXContract.CREATEMARKETORDER -> {
                    isSend = false
                    noticeDialog.showNotice(8)
                    EventBus.getDefault().post(MarketSellSuccessEvent(0))
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        isSend = false
        em_network_view.visibility = View.GONE
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
        isSend = false
        if(isRefreshList){
            em_network_view.visibility = View.VISIBLE
        }else{
            toast("请检查网络连接").setGravity(Gravity.CENTER, 0, 0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

}