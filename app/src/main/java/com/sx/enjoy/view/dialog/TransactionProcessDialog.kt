package com.sx.enjoy.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sx.enjoy.R
import kotlinx.android.synthetic.main.dialog_transaction_process.*

class TransactionProcessDialog : Dialog {

    private var orderId = ""
    private var type = 0

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_transaction_process)

        initEvent()
    }

    fun showNotice(orderId:String,type:Int){
        show()
        this.orderId = orderId
        this.type = type
    }

    private fun initEvent(){
        tv_confirm.setOnClickListener {
            dismiss()
            mOnNoticeConfirmListener?.onConfirm(orderId,type)
        }
    }

    interface OnNoticeConfirmListener{
        fun onConfirm(orderId:String,type:Int)
    }

    private var mOnNoticeConfirmListener : OnNoticeConfirmListener? = null

    fun setOnNoticeConfirmListener(mOnNoticeConfirmListener : OnNoticeConfirmListener){
        this.mOnNoticeConfirmListener = mOnNoticeConfirmListener
    }

}