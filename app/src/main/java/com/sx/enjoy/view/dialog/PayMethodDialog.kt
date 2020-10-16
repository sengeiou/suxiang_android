package com.sx.enjoy.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sx.enjoy.R
import kotlinx.android.synthetic.main.dialog_notice_view.tv_confirm
import kotlinx.android.synthetic.main.dialog_pay_menthod.*

class PayMethodDialog : Dialog {

    private var payCode = ""

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pay_menthod)

        initEvent()
    }


    fun showPrice(price:String,payCode:String){
        show()
        this.payCode = payCode
        tv_order_price.text = "¥${price}"
    }

    private fun initEvent(){
        iv_close.setOnClickListener {
            dismiss()
        }
        tv_confirm.setOnClickListener {
            dismiss()
            mOnNoticeConfirmListener?.onConfirm(payCode)
        }
    }

    interface OnNoticeConfirmListener{
        fun onConfirm(payCode:String)
    }

    private var mOnNoticeConfirmListener : OnNoticeConfirmListener? = null

    fun setOnNoticeConfirmListener(mOnNoticeConfirmListener : OnNoticeConfirmListener){
        this.mOnNoticeConfirmListener = mOnNoticeConfirmListener
    }

}