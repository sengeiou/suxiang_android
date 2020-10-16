package com.sx.enjoy.modules.store

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.View
import com.alipay.sdk.app.PayTask
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.AuthUserBean
import com.sx.enjoy.bean.NewOrderBean
import com.sx.enjoy.bean.PayResult
import com.sx.enjoy.bean.PayResultBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.UserAuthSuccessEvent
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import kotlinx.android.synthetic.main.activity_order_pay.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class OrderPayActivity : BaseActivity() , SXContract.View{

    private lateinit var present: SXPresent

    private var payMethod = 1
    private var orderNo = ""

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"选择支付方式")

    override fun getLayoutResource() = R.layout.activity_order_pay

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        val order = intent.getSerializableExtra("order") as NewOrderBean
        tv_price.text = order.payPrice
        orderNo = order.orderNo

       initEvent()
    }

    private fun initEvent(){
        bt_pay.setOnClickListener {
            present.orderPay(orderNo,payMethod.toString(),"order")
        }

        ll_pay_zfb.setOnClickListener {
            tb_zfb.isChecked = true
            tb_wx.isChecked = false
            payMethod = 1
        }

        ll_pay_wx.setOnClickListener {
            toast("暂不支持微信支付")
//            tb_wx.isChecked = true
//            tb_zfb.isChecked = false
//            payMethod = 0
        }
    }

    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            val result = PayResult( msg.obj as (Map<String, String>))
            val resultInfo = result.result
            val resultStatus = result.resultStatus
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                startActivity<PayResultActivity>(Pair("resultCode",1),Pair("orderNo",orderNo))
            }else{
                startActivity<PayResultActivity>(Pair("resultCode",0),Pair("orderNo",orderNo))
            }
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.ORDERPAY -> {
                    data.let {
                        data as PayResultBean
                        val payRunnable = Runnable {
                            val aliPay = PayTask(this)
                            val result = aliPay.payV2(data.aliPay, true)

                            val msg = Message()
                            msg.obj = result
                            mHandler.sendMessage(msg)
                        }
                        val payThread = Thread(payRunnable)
                        payThread.start()
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