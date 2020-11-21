package com.sx.enjoy.modules.login

import android.app.Activity
import android.content.Intent
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Gravity
import com.likai.lib.commonutils.SharedPreferencesUtil
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.UserStateChangeEvent
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.EncryptionUtil
import com.sx.enjoy.utils.RegularUtil
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_user_phone
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

class LoginActivity : BaseActivity() ,SXContract.View{

    private lateinit var present: SXPresent

    private var isShowPassword = false

    private var lastClickTime = 0L

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"")

    override fun getLayoutResource() = R.layout.activity_login

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        val savePhone = SharedPreferencesUtil.getCommonString(this,"savePhone")
        if(savePhone.isNotEmpty()){
            et_user_phone.setText(savePhone)
        }

        initEvent()
    }

    private fun initEvent(){
        ll_password_status.setOnClickListener {
            isShowPassword = !isShowPassword
            if (isShowPassword) {
                iv_password_status.setImageResource(R.mipmap.ic_password_show)
                et_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                iv_password_status.setImageResource(R.mipmap.ic_password_hide)
                et_password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            et_password.setSelection(et_password.text.length)
        }

        ll_go_register.setOnClickListener {
            startActivityForResult<RegisterActivity>(1009)
        }
        ll_password_forget.setOnClickListener {
            startActivity<PasswordForgetActivity>()
        }
        tv_login.setOnClickListener {
            if(System.currentTimeMillis()-lastClickTime>1000){
                lastClickTime = System.currentTimeMillis()
                if(et_user_phone.text.isEmpty()){
                    toast("请输入手机号").setGravity(Gravity.CENTER, 0, 0)
                    return@setOnClickListener
                }
                if(!RegularUtil.isChinaPhoneLegal(et_user_phone.text.toString())){
                    toast("手机号不正确").setGravity(Gravity.CENTER, 0, 0)
                    return@setOnClickListener
                }
                if(et_password.text.isEmpty()){
                    toast("请输入密码").setGravity(Gravity.CENTER, 0, 0)
                    return@setOnClickListener
                }
                present.login(et_user_phone.text.toString(),EncryptionUtil.MD5(et_password.text.toString()),SharedPreferencesUtil.getCommonString(this,"RegistrationID"),"Android","")
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == RESULT_OK&&requestCode == 1009){
            if(data != null && data.getBooleanExtra("isRegister",false)){
                et_user_phone.setText(data.getStringExtra("phone"))
            }
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.LOGIN -> {
                    data?.let {
                        data as UserBean
                        data.userId = data.id.toString()
                        data.setIsWxPay(data.isWxPay)
                        data.setIsAliPay(data.isAliPay)
                        data.setActivity(data.userActivity)
                        data.setContrib(data.userContrib)
                        data.setUserExperience(data.experience)
                        data.setIsPayPwd(data.isPayPwd)
                        C.USER_ID = data.userId
                        data.save()
                        SharedPreferencesUtil.putCommonString(this,"savePhone",data.userPhone)
                        EventBus.getDefault().post(UserStateChangeEvent(1))
                        MobclickAgent.onProfileSignIn(data.userId)
                        finish()
                    }
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        toast(string!!).setGravity(Gravity.CENTER, 0, 0)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(boolean){
            toast("请检查网络连接").setGravity(Gravity.CENTER, 0, 0)
        }
    }

}