package com.sx.enjoy.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sx.enjoy.R
import kotlinx.android.synthetic.main.dialog_change_vip.*

class VIPChangeDialog : Dialog {

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_change_vip)

        initEvent()
    }

    fun showVIPChange(rice:String,curCount:String,totalCount:String){
        show()
        tv_rice_sub.text = "-${rice}"
        tv_rice_notice.text = "您将消耗${rice}米粒兑换VIP1 是否兑换？"
        tv_change_count.text = "${curCount}/${totalCount}"
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