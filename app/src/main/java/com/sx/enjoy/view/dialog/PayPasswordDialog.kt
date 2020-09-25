package com.sx.enjoy.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sx.enjoy.R
import kotlinx.android.synthetic.main.dialog_pay_password.*

class PayPasswordDialog : Dialog {

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pay_password)

        initEvent()
    }


    fun showInputPassword(){
        show()
        et_password.setText("")
    }

    private fun initEvent(){
        iv_close.setOnClickListener {
            dismiss()
        }
        tv_password_save.setOnClickListener {
            dismiss()
            mOnNoticeConfirmListener?.onConfirm(et_password.text.toString())
        }
    }

    interface OnNoticeConfirmListener{
        fun onConfirm(password:String)
    }

    private var mOnNoticeConfirmListener : OnNoticeConfirmListener? = null

    fun setOnNoticeConfirmListener(mOnNoticeConfirmListener : OnNoticeConfirmListener){
        this.mOnNoticeConfirmListener = mOnNoticeConfirmListener
    }

}