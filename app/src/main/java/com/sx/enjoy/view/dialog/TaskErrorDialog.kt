package com.sx.enjoy.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sx.enjoy.R
import kotlinx.android.synthetic.main.dialog_notice_view.iv_close
import kotlinx.android.synthetic.main.dialog_notice_view.tv_confirm
import kotlinx.android.synthetic.main.dialog_notice_view.tv_notice_text

class TaskErrorDialog : Dialog {

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_task_error_view)

        initEvent()
    }


    fun showNotice(introduce:String){
        show()
        tv_notice_text.text = introduce
    }

    private fun initEvent(){
        iv_close.setOnClickListener {
            dismiss()
        }
        tv_confirm.setOnClickListener {
            dismiss()
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