package com.sx.enjoy.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sx.enjoy.R
import kotlinx.android.synthetic.main.dialog_get_task.*

class TaskGetDialog : Dialog {

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_get_task)

        initEvent()
    }

    fun showTaskGet(riceCount:String){
        show()
        tv_rice_sub.text = "-$riceCount"
        tv_rice_notice.text = "你将消耗${riceCount}米粒 是否兑换？"
    }

    private fun initEvent(){
        iv_close.setOnClickListener {
            dismiss()
        }
        tv_close.setOnClickListener {
            dismiss()
        }
        tv_confirm.setOnClickListener {
            dismiss()
            mOnNoticeConfirmListener?.onConfirm()
        }
    }

    interface OnNoticeConfirmListener{
        fun onConfirm()
    }

    private var mOnNoticeConfirmListener : OnNoticeConfirmListener? = null

    fun setOnNoticeConfirmListener(mOnNoticeConfirmListener : OnNoticeConfirmListener){
        this.mOnNoticeConfirmListener = mOnNoticeConfirmListener
    }

}