package com.sx.enjoy.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import com.sx.enjoy.R
import com.sx.enjoy.bean.UpdateInfoBean
import kotlinx.android.synthetic.main.dialog_apk_update.*

class ApkUpdateDialog : Dialog {

    private lateinit var info : UpdateInfoBean

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_apk_update)
        initEvent()
    }


    fun showUpdate(info :UpdateInfoBean){
        show()
        this.info = info
        tv_content.text = info.updateContent
        tv_close.visibility = if(info.isMust==0) View.VISIBLE else View.GONE
        setCanceledOnTouchOutside(info.isMust==0)
    }

    private fun initEvent(){
        tv_update.setOnClickListener {
            mOnApkDownloadConfirmListener?.onConfirmDownload(info)
        }
        tv_close.setOnClickListener {
            dismiss()
        }
    }

    interface OnApkDownloadConfirmListener{
        fun onConfirmDownload(info :UpdateInfoBean)
    }

    private var mOnApkDownloadConfirmListener:OnApkDownloadConfirmListener? = null

    fun setOnApkDownloadConfirmListener(mOnApkDownloadConfirmListener:OnApkDownloadConfirmListener){
        this.mOnApkDownloadConfirmListener = mOnApkDownloadConfirmListener
    }

}