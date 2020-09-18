package com.sx.enjoy.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sx.enjoy.R
import kotlinx.android.synthetic.main.dialog_sex_select.*

class SexSelectDialog : Dialog {

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_sex_select)

        initEvent()
    }


    private fun initEvent(){
        tv_select_man.setOnClickListener {
            mOnNoticeConfirmListener?.onConfirm(1)
            dismiss()
        }
        tv_select_woman.setOnClickListener {
            mOnNoticeConfirmListener?.onConfirm(0)
            dismiss()
        }
    }

    interface OnNoticeConfirmListener{
        fun onConfirm(sex:Int)
    }

    private var mOnNoticeConfirmListener : OnNoticeConfirmListener? = null

    fun setOnNoticeConfirmListener(mOnNoticeConfirmListener : OnNoticeConfirmListener){
        this.mOnNoticeConfirmListener = mOnNoticeConfirmListener
    }

}