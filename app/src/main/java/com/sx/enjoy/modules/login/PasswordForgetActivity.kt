package com.sx.enjoy.modules.login

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.utils.TimeCountUtil
import com.sx.enjoy.view.dialog.NoticeDialog
import kotlinx.android.synthetic.main.activity_password_forget.*

class PasswordForgetActivity : BaseActivity()  ,TimeCountUtil.OnCountDownListener{

    private lateinit var time: TimeCountUtil
    private lateinit var noticeDialog: NoticeDialog

    private var isShowPassword1 = false
    private var isShowPassword2 = false

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"")

    override fun getLayoutResource() = R.layout.activity_password_forget

    override fun initView() {
        noticeDialog = NoticeDialog(this)

        time = TimeCountUtil(60000,1000)
        time.setOnCountDownListener(this)


        initEvent()
    }
    private fun initEvent(){
        ll_password_status_1.setOnClickListener {
            isShowPassword1 = !isShowPassword1
            if (isShowPassword1) {
                iv_password_status_1.setImageResource(R.mipmap.ic_password_show)
                et_password_1.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                iv_password_status_1.setImageResource(R.mipmap.ic_password_hide)
                et_password_1.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            et_password_1.setSelection(et_password_1.text.length)
        }
        ll_password_status_2.setOnClickListener {
            isShowPassword2 = !isShowPassword2
            if (isShowPassword2) {
                iv_password_status_2.setImageResource(R.mipmap.ic_password_show)
                et_password_2.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                iv_password_status_2.setImageResource(R.mipmap.ic_password_hide)
                et_password_2.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            et_password_2.setSelection(et_password_2.text.length)
        }
        tv_forget_code.setOnClickListener {
            time.start()
        }
        tv_forget.setOnClickListener {
            noticeDialog.showNotice(2)
        }
    }

    override fun onTickChange(millisUntilFinished: Long) {
        tv_forget_code.text = (millisUntilFinished/1000).toString()+"s"
    }

    override fun OnFinishChanger() {
        tv_forget_code.text = "重新发送"
    }

    override fun onDestroy() {
        super.onDestroy()
        time.cancel()
    }
}