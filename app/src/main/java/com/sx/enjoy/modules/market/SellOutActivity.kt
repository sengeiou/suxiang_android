package com.sx.enjoy.modules.market

import android.app.Activity
import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.RiceRangeBean
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.MarketSellSuccessEvent
import com.sx.enjoy.modules.mine.AuthenticationActivity
import com.sx.enjoy.modules.mine.PayMethodSetActivity
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.ImageLoaderUtil
import com.sx.enjoy.utils.RegularUtil
import com.sx.enjoy.view.dialog.NormalSetDialog
import com.sx.enjoy.view.dialog.NoticeDialog
import com.sx.enjoy.view.dialog.ReminderDialog
import kotlinx.android.synthetic.main.activity_sell_out.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast
import org.litepal.LitePal

class SellOutActivity : BaseActivity() , SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var noticeDialog : NoticeDialog
    private lateinit var reminderDialog: ReminderDialog
    private lateinit var normalSetDialog: NormalSetDialog

    private var isSend = false
    private var errorCode = "0"

    private var isAliPay = false
    private var isWxPay = false

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"我要卖出")

    override fun getLayoutResource() = R.layout.activity_sell_out

    override fun initView() {
        noticeDialog = NoticeDialog(this)
        reminderDialog = ReminderDialog(this)
        normalSetDialog = NormalSetDialog(this)

        present = SXPresent(this)

        initEvent()

        initUserInfo()

        present.getRiceRange()
        present.getRichFee(C.USER_ID)
    }

    private fun initUserInfo(){
        val user = LitePal.findLast(UserBean::class.java)
        if(user!=null){
            isAliPay = user.isAliPay
            isWxPay = user.isWxPay
            if(user.isAliPay){
                ll_zfb_number.visibility = View.VISIBLE
                ll_zfb_code.visibility = View.VISIBLE
                v_zfb_line.visibility = View.VISIBLE
                tv_ali_number.text = user.aliNumber
                ImageLoaderUtil().displayImage(this,user.payQrcode,iv_zfb_code)
            }
            if(user.isWxPay){
                ll_wx_code.visibility = View.VISIBLE
                v_wx_line.visibility = View.VISIBLE
                ImageLoaderUtil().displayImage(this,user.wxQrcode,iv_wx_code)
            }
        }
    }

    private fun initEvent(){
        tv_submit.setOnClickListener {
            if(et_sell_count.text.isEmpty()){
                toast("请输入卖出数量").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(!RegularUtil.isNumber(et_sell_count.text.toString())){
                toast("数量输入有误").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(et_sell_price.text.isEmpty()){
                toast("请输入卖出单价").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(!RegularUtil.isNumber(et_sell_price.text.toString())){
                toast("单价输入有误").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(!isAliPay&&!isWxPay){
                normalSetDialog.showNotice(1)
                return@setOnClickListener
            }
            if(!isSend){
                isSend = true
                present.publishMarketInfo(C.USER_ID,C.MARKET_ORDER_STATUS_SELL.toString(),et_sell_price.text.toString(),et_sell_count.text.toString(),if(isAliPay) "1" else "0",if(isWxPay) "1" else "0")
            }
        }

        et_sell_count.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(et_sell_count.text.isNotEmpty()&&et_sell_price.text.isNotEmpty()){
                    if(RegularUtil.isNumber(et_sell_count.text.toString())&&RegularUtil.isNumber(et_sell_price.text.toString())){
                        tv_rice_total.text =  String.format("%.2f", et_sell_count.text.toString().toDouble()*et_sell_price.text.toString().toDouble())
                    }
                }else{
                    tv_rice_total.text = ""
                }
            }
        })
        et_sell_price.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(et_sell_count.text.isNotEmpty()&&et_sell_price.text.isNotEmpty()){
                    if(RegularUtil.isNumber(et_sell_count.text.toString())&&RegularUtil.isNumber(et_sell_price.text.toString())){
                        tv_rice_total.text =  String.format("%.2f", et_sell_count.text.toString().toDouble()*et_sell_price.text.toString().toDouble())
                    }
                }else{
                    tv_rice_total.text = ""
                }
            }
        })

        noticeDialog.setOnDismissListener {
            finish()
        }
        reminderDialog.setOnNoticeConfirmListener(object :ReminderDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                if(errorCode == "12"){
                    startActivity<AuthenticationActivity>()
                }
            }
        })
        normalSetDialog.setOnNoticeConfirmListener(object:NormalSetDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                startActivityForResult<PayMethodSetActivity>(4001)
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK&&requestCode == 4001){
            initUserInfo()
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
                SXContract.GETRICERANGE -> {
                    data?.let {
                        data as RiceRangeBean
                        tv_rice_range.text = "注:输入米粒价格范围${data.richPriceMin}至${data.richPriceMax}"
                    }
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
        if(boolean){
            toast("请检查网络连接").setGravity(Gravity.CENTER, 0, 0)
        }
    }

}