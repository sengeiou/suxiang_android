package com.sx.enjoy.modules.login

import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.EncryptionUtil
import com.sx.enjoy.utils.RegularUtil
import com.sx.enjoy.utils.TimeCountUtil
import com.sx.enjoy.view.dialog.NoticeDialog
import kotlinx.android.synthetic.main.activity_password_forget.*
import kotlinx.android.synthetic.main.activity_password_forget.et_password_1
import kotlinx.android.synthetic.main.activity_password_forget.et_password_2
import kotlinx.android.synthetic.main.activity_password_forget.iv_password_status_1
import kotlinx.android.synthetic.main.activity_password_forget.iv_password_status_2
import kotlinx.android.synthetic.main.activity_password_forget.ll_password_status_1
import kotlinx.android.synthetic.main.activity_password_forget.ll_password_status_2
import org.jetbrains.anko.toast

class PasswordForgetActivity : BaseActivity()  ,TimeCountUtil.OnCountDownListener,SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var time: TimeCountUtil
    private lateinit var noticeDialog: NoticeDialog

    private var isShowPassword1 = false
    private var isShowPassword2 = false
    private var isGetCode = false

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"")

    override fun getLayoutResource() = R.layout.activity_password_forget

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

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
            if(!isGetCode){
                if(et_user_phone.text.isEmpty()){
                    toast("请输入手机号")
                    return@setOnClickListener
                }
                if(!RegularUtil.isChinaPhoneLegal(et_user_phone.text.toString())){
                    toast("手机号不正确")
                    return@setOnClickListener
                }
                present.sendCode(et_user_phone.text.toString(),"1")
            }
        }
        tv_forget.setOnClickListener {
            if(et_user_phone.text.isEmpty()){
                toast("请输入手机号")
                return@setOnClickListener
            }
            if(!RegularUtil.isChinaPhoneLegal(et_user_phone.text.toString())){
                toast("手机号不正确")
                return@setOnClickListener
            }
            if(et_code.text.isEmpty()){
                toast("请输入验证码")
                return@setOnClickListener
            }
            if(et_password_1.text.isEmpty()){
                toast("请输入密码")
                return@setOnClickListener
            }
            if(et_password_2.text.isEmpty()){
                toast("请再次输入密码")
                return@setOnClickListener
            }
            if(et_password_1.text.toString()!=et_password_2.text.toString()){
                toast("密码不一致")
                return@setOnClickListener
            }
            present.forgetPassword(et_user_phone.text.toString(),et_code.text.toString(),
                EncryptionUtil.MD5(et_password_1.text.toString()),EncryptionUtil.MD5(et_password_2.text.toString()))
        }
        noticeDialog.setOnDismissListener {
            finish()
        }
    }

    override fun onTickChange(millisUntilFinished: Long) {
        tv_forget_code.text = (millisUntilFinished/1000).toString()+"s"
    }

    override fun OnFinishChanger() {
        tv_forget_code.text = "重新发送"
        isGetCode = false
    }

    override fun onDestroy() {
        super.onDestroy()
        time.cancel()
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.SENDCODE -> {
                    time.start()
                    isGetCode = true
                }
                SXContract.FORGETPASSWORD -> {
                    noticeDialog.showNotice(2)
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        toast(string!!)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(boolean){
            toast("请检查网络连接")
        }
    }

}