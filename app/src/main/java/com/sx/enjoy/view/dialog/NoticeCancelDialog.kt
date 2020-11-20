package com.sx.enjoy.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import com.sx.enjoy.R
import kotlinx.android.synthetic.main.dialog_notice_cancel.*

class NoticeCancelDialog : Dialog {

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_notice_cancel)

        initEvent()
    }


    fun showNotice(cur:Int,total:Int,isConfirm:Boolean){
        show()
        val line1 = "今日取消次数$cur/$total"
        val line2 = "今日取消次数累计${total}次将暂停您的交易，预计后天解除。"
        val sp1 = SpannableStringBuilder(line1)
        val sp2 = SpannableStringBuilder(line2)
        sp1.setSpan(ForegroundColorSpan(context.resources.getColor(R.color.main_color)),6,line1.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        sp2.setSpan(ForegroundColorSpan(context.resources.getColor(R.color.main_color)),8,9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tv_notice_text_1.text = sp1
        tv_notice_text_2.text = sp2

        tv_confirm.visibility = if(isConfirm) View.VISIBLE else View.INVISIBLE
    }


    private fun initEvent(){
        tv_close.setOnClickListener {
            dismiss()
        }
        tv_confirm.setOnClickListener {
            mOnNoticeConfirmListener?.onConfirm()
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