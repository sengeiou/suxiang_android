package com.sx.enjoy.modules.mine

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.alipay.sdk.app.PayTask
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.AuthUserBean
import com.sx.enjoy.bean.PayResult
import com.sx.enjoy.bean.PayResultBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.UserAuthSuccessEvent
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.RegularUtil
import com.sx.enjoy.view.dialog.NoticeDialog
import kotlinx.android.synthetic.main.activity_authentication.*
import kotlinx.android.synthetic.main.activity_authentication.iv_close
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class AuthenticationActivity : BaseActivity() ,SXContract.View{

    private lateinit var present: SXPresent

    private var payBottomSheet: BottomSheetBehavior<View>? = null

    private lateinit var noticeDialog: NoticeDialog

    private var authBean : AuthUserBean ? = null

    private var payMethod = 1

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"实名认证")

    override fun getLayoutResource() = R.layout.activity_authentication

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        present.getAuthUser(C.USER_ID)

        payBottomSheet = BottomSheetBehavior.from(findViewById(R.id.bs_pay_view))

        noticeDialog = NoticeDialog(this)

        tv_auth.setOnClickListener {
            if(authBean == null){
                return@setOnClickListener
            }
            if(authBean!!.status != 0&&authBean!!.status != 3){
                toast("您已成功提交认证，无需重复提交").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(et_user_id.text.isEmpty()){
                toast("请输入身份证号").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(et_user_name.text.isEmpty()){
                toast("请输入姓名").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(et_user_phone.text.isEmpty()){
                toast("请输入手机号").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(!RegularUtil.isChinaPhoneLegal(et_user_phone.text.toString())){
                toast("手机号不正确").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(et_user_phone.windowToken,0)
            payBottomSheet?.state = BottomSheetBehavior.STATE_EXPANDED
        }

        ll_pay_zfb.setOnClickListener {
            tb_zfb.isChecked = true
            tb_wx.isChecked = false
            payMethod = 1
        }

        ll_pay_wx.setOnClickListener {
            toast("暂不支持微信支付").setGravity(Gravity.CENTER, 0, 0)
//            tb_wx.isChecked = true
//            tb_zfb.isChecked = false
//            payMethod = 0
        }

        tv_confirm.setOnClickListener {
            payBottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
            present.authUser(C.USER_ID,et_user_id.text.toString(),et_user_name.text.toString(),et_user_phone.text.toString(),payMethod.toString())
        }

        iv_close.setOnClickListener {
            payBottomSheet?.state = BottomSheetBehavior.STATE_HIDDEN
        }

        ll_pay_content.setOnClickListener {  }
    }


    private val mHandler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            val result = PayResult( msg.obj as (Map<String, String>))
            val resultInfo = result.result
            val resultStatus = result.resultStatus
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                noticeDialog.showNotice(11)
            }else{
                toast("支付失败").setGravity(Gravity.CENTER, 0, 0)
            }
            EventBus.getDefault().post(UserAuthSuccessEvent(1))
            present.getAuthUser(C.USER_ID)
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETAUTHUSER -> {
                    data.let {
                        data as AuthUserBean
                        authBean = data
                        ll_auth_price.visibility = View.VISIBLE
                        et_user_id.setText(data.idNumber)
                        et_user_name.setText(data.name)
                        et_user_phone.setText(data.phone)
                        when(data.status){
                            0 -> {
                                et_user_id.isEnabled = true
                                et_user_name.isEnabled = true
                                et_user_phone.isEnabled = true
                                tv_auth.visibility = View.VISIBLE
                                tv_pay_money.text = "*认证需要支付认证费${data.price}元"
                                tv_auth.text = "立即认证"
                            }
                            1 -> {
                                et_user_id.isEnabled = false
                                et_user_name.isEnabled = false
                                et_user_phone.isEnabled = false
                                tv_auth.visibility = View.GONE
                                tv_pay_money.text = "待审核"
                            }
                            2 -> {
                                et_user_id.isEnabled = false
                                et_user_name.isEnabled = false
                                et_user_phone.isEnabled = false
                                tv_auth.visibility = View.GONE
                                tv_pay_money.text = "已认证"
                            }
                            3 -> {
                                et_user_id.isEnabled = true
                                et_user_name.isEnabled = true
                                et_user_phone.isEnabled = true
                                tv_auth.visibility = View.VISIBLE
                                tv_pay_money.text = "已拒绝"
                                tv_auth.text = "重新认证"
                            }
                        }

                    }
                }
                SXContract.AUTHUSER -> {
                    data.let {
                        data as PayResultBean
                        val payRunnable = Runnable {
                            val aliPay = PayTask(this)
                            val result = aliPay.payV2(data.aliPay, true)

                            val msg = Message()
                            msg.obj = result
                            mHandler.sendMessage(msg)
                        }
                        val payThread = Thread(payRunnable)
                        payThread.start()
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