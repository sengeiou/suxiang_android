package com.sx.enjoy.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sx.enjoy.R
import kotlinx.android.synthetic.main.dialog_reminder_view.*

class ReminderDialog : Dialog {

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_reminder_view)

        initEvent()
    }


    fun showReminder(index:Int){
        show()
        when(index){
            1 -> {
                tv_reminder_text.text = "是否退出登录？"
            }
        }
    }

    private fun initEvent(){
        tv_cancel.setOnClickListener {
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