package com.sx.enjoy.modules.market

import android.app.Activity
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.RiceRangeBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.MarketSellSuccessEvent
import com.sx.enjoy.modules.mine.AuthenticationActivity
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.RegularUtil
import com.sx.enjoy.view.dialog.NoticeDialog
import com.sx.enjoy.view.dialog.ReminderDialog
import kotlinx.android.synthetic.main.activity_buy_in.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class BuyInActivity : BaseActivity() ,SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var noticeDialog : NoticeDialog
    private lateinit var reminderDialog: ReminderDialog

    private var isSend = false
    private var errorCode = "0"

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"我要买入")

    override fun getLayoutResource() = R.layout.activity_buy_in

    override fun initView() {
        noticeDialog = NoticeDialog(this)
        reminderDialog = ReminderDialog(this)

        present = SXPresent(this)

        present.getRiceRange()

        initEvent()
    }

    private fun initEvent(){
        tv_submit.setOnClickListener {
            if(et_rice_count.text.isEmpty()){
                toast("请输入买入数量").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(!RegularUtil.isNumber(et_rice_count.text.toString())){
                toast("数量输入有误").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(et_rice_price.text.isEmpty()){
                toast("请输入买入单价").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(!RegularUtil.isNumber(et_rice_price.text.toString())){
                toast("单价输入有误").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(!tb_checked_zfb.isChecked&&!tb_checked_wx.isChecked){
                toast("请选择支付方式").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(!isSend){
                isSend = true
                present.publishMarketInfo(C.USER_ID,"0",et_rice_price.text.toString(),et_rice_count.text.toString(),if(tb_checked_zfb.isChecked) "1" else "0",if(tb_checked_wx.isChecked) "1" else "0")
            }
        }

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

        et_rice_count.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(et_rice_count.text.isNotEmpty()&&et_rice_price.text.isNotEmpty()){
                    if(RegularUtil.isNumber(et_rice_count.text.toString())&&RegularUtil.isNumber(et_rice_price.text.toString())){
                        tv_rice_total.text =  String.format("%.2f", et_rice_count.text.toString().toDouble()*et_rice_price.text.toString().toDouble())
                    }
                }else{
                    tv_rice_total.text = ""
                }
            }
        })
        et_rice_price.addTextChangedListener(object :TextWatcher{
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if(et_rice_count.text.isNotEmpty()&&et_rice_price.text.isNotEmpty()){
                    if(RegularUtil.isNumber(et_rice_count.text.toString())&&RegularUtil.isNumber(et_rice_price.text.toString())){
                        tv_rice_total.text =  String.format("%.2f", et_rice_count.text.toString().toDouble()*et_rice_price.text.toString().toDouble())
                    }
                }else{
                    tv_rice_total.text = ""
                }
            }
        })
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.PUBLISHMARKETINFO -> {
                    isSend = false
                    noticeDialog.showNotice(3)
                    EventBus.getDefault().post(MarketSellSuccessEvent(0))
                }
                SXContract.GETRICERANGE -> {
                    data?.let {
                        data as RiceRangeBean
                        tv_rice_range.text = "注:输入米粒价格范围${data.richPriceMin}至${data.richPriceMax}"
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