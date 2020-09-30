package com.sx.enjoy.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sx.enjoy.R
import kotlinx.android.synthetic.main.dialog_notice_view.*

class NoticeDialog : Dialog {

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_notice_view)

        initEvent()
    }


    fun showNotice(index:Int){
        show()
        when(index){
            1 -> {
                tv_notice_text.text = "注册成功"
            }
            2 -> {
                tv_notice_text.text = "保存成功"
            }
            3 -> {
                tv_notice_text.text = "发布成功"
            }
            4 -> {
                tv_notice_text.text = "修改成功"
            }
            5 -> {
                iv_notice_view.setImageResource(R.mipmap.ic_task_success)
                tv_notice_text.text = "兑换成功!,获取1活跃值"
            }
            6 -> {
                tv_notice_text.text = "卖出成功"
            }
            7 -> {
                tv_notice_text.text = "提交成功"
            }
            8 -> {
                tv_notice_text.text = "买入成功"
            }
            9 -> {
                tv_notice_text.text = "操作成功"
            }
        }
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