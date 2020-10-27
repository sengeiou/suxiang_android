package com.sx.enjoy.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.sx.enjoy.R
import kotlinx.android.synthetic.main.dialog_apk_update.*

class OpenAuthorityDialog : Dialog {


    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_apk_update)
        initEvent()
    }


    fun showOpenAuthority(){
        show()
        tv_title.text = "提示"
        tv_content.text = "安装应用需要打开未知来源权限，请去设置中开启权限"
        tv_update.text = "立即开启"
        tv_close.text = "取消"
    }

    private fun initEvent(){
        tv_update.setOnClickListener {
            mOnOpenAuthorityConfirmListener?.onConfirmDownload()
            dismiss()
        }
        tv_close.setOnClickListener {
            dismiss()
        }
    }

    interface OnOpenAuthorityConfirmListener{
        fun onConfirmDownload()
    }

    private var mOnOpenAuthorityConfirmListener:OnOpenAuthorityConfirmListener? = null

    fun setOnApkDownloadConfirmListener(mOnOpenAuthorityConfirmListener:OnOpenAuthorityConfirmListener){
        this.mOnOpenAuthorityConfirmListener = mOnOpenAuthorityConfirmListener
    }

}