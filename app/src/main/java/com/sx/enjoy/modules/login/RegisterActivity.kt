package com.sx.enjoy.modules.login

import android.os.Message
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import com.likai.lib.commonutils.SharedPreferencesUtil
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.RegularUtil
import com.sx.enjoy.utils.TimeCountUtil
import com.sx.enjoy.view.dialog.NoticeDialog
import kotlinx.android.synthetic.main.activity_register.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class RegisterActivity : BaseActivity() ,TimeCountUtil.OnCountDownListener ,SXContract.View {

    private lateinit var present: SXPresent

    private lateinit var time: TimeCountUtil
    private lateinit var noticeDialog: NoticeDialog

    private var isShowPassword1 = false
    private var isShowPassword2 = false

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL, "")

    override fun getLayoutResource(): Int = R.layout.activity_register

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        noticeDialog = NoticeDialog(this)

        time = TimeCountUtil(60000, 1000)
        time.setOnCountDownListener(this)


        initEvent()

    }

    private fun initEvent() {
        tv_go_login.setOnClickListener {
            finish()
        }
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
        tv_register_code.setOnClickListener {
            if(et_user_phone.text.isEmpty()){
                toast("请输入手机号")
                return@setOnClickListener
            }
            if(!RegularUtil.isChinaPhoneLegal(et_user_phone.text.toString())){
                toast("手机号不正确")
                return@setOnClickListener
            }
            present.sendCode(et_user_phone.text.toString(),"0")
        }
        tv_register.setOnClickListener {
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
            if(et_password_1.text.toString() != et_password_2.text.toString()){
                toast("密码不一致")
                return@setOnClickListener
            }
            present.register(et_code.text.toString(),et_recommend_code.text.toString(),et_password_1.text.toString(),et_password_2.text.toString(),et_user_phone.text.toString())
        }
        noticeDialog.setOnDismissListener {
            finish()
        }
    }

    override fun onTickChange(millisUntilFinished: Long) {
        tv_register_code.text = (millisUntilFinished / 1000).toString() + "s"
    }

    override fun OnFinishChanger() {
        tv_register_code.text = "重新发送"
    }

    override fun onDestroy() {
        super.onDestroy()
        time.cancel()
    }


    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.SENDCODE -> {
                    data?.let {
                        data as String
                        time.start()
                    }
                }
                SXContract.REGISTER -> {
                    data?.let {
                        data as String
                        noticeDialog.showNotice(1)
                    }
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