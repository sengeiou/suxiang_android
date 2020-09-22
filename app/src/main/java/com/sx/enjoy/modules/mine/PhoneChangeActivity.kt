package com.sx.enjoy.modules.mine

import android.app.Activity
import android.content.Intent
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.SwitchPagerEvent
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.RegularUtil
import com.sx.enjoy.utils.TimeCountUtil
import com.sx.enjoy.view.dialog.NoticeDialog
import kotlinx.android.synthetic.main.activity_phone_change.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.toast

class PhoneChangeActivity : BaseActivity() ,SXContract.View {

    private lateinit var noticeDialog: NoticeDialog

    private lateinit var present: SXPresent

    private lateinit var timeOld: TimeCountUtil
    private lateinit var timeNew: TimeCountUtil
    private var isSendOld = false
    private var isSendNew = false

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"更换手机号")

    override fun getLayoutResource() = R.layout.activity_phone_change

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        noticeDialog = NoticeDialog(this)
        timeOld = TimeCountUtil(60000,1000)
        timeNew = TimeCountUtil(60000,1000)

        tv_user_phone.text = intent.getStringExtra("phone")

        initEvent()
    }

    private fun initEvent(){
        tv_new_code.setOnClickListener {
            if(isSendNew){
                return@setOnClickListener
            }
            if(et_new_phone.text.isEmpty()){
                toast("请输入新手机号")
                return@setOnClickListener
            }
            if(!RegularUtil.isChinaPhoneLegal(et_new_phone.text.toString())){
                toast("手机号不正确")
                return@setOnClickListener
            }
            present.sendCode(et_new_phone.text.toString(),"3")
        }
        tv_old_code.setOnClickListener {
            if(isSendOld){
                return@setOnClickListener
            }
            present.sendCode(tv_user_phone.text.toString(),"2")
        }
        timeOld.setOnCountDownListener(object : TimeCountUtil.OnCountDownListener{
            override fun onTickChange(millisUntilFinished: Long) {
                tv_old_code.text = (millisUntilFinished/1000).toString()+"s"
            }

            override fun OnFinishChanger() {
                tv_old_code.text = "重新发送"
                isSendOld = false
            }
        })
        timeNew.setOnCountDownListener(object : TimeCountUtil.OnCountDownListener{
            override fun onTickChange(millisUntilFinished: Long) {
                tv_new_code.text = (millisUntilFinished/1000).toString()+"s"
            }

            override fun OnFinishChanger() {
                tv_new_code.text = "重新发送"
                isSendNew = false
            }
        })
        noticeDialog.setOnDismissListener {
            finish()
        }
        tv_save_phone.setOnClickListener {
            if(et_old_code.text.isEmpty()){
                toast("请输入原手机号验证码")
                return@setOnClickListener
            }
            if(et_new_phone.text.isEmpty()){
                toast("请输入新手机号")
                return@setOnClickListener
            }
            if(!RegularUtil.isChinaPhoneLegal(et_new_phone.text.toString())){
                toast("新手机号不正确")
                return@setOnClickListener
            }
            if(et_new_code.text.isEmpty()){
                toast("请输入新手机号验证码")
                return@setOnClickListener
            }
            present.updateUserPhone(tv_user_phone.text.toString(),et_old_code.text.toString(),et_new_phone.text.toString(),et_new_code.text.toString())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timeNew.cancel()
        timeOld.cancel()
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.SENDCODE+"2" -> {
                    isSendOld = true
                    timeOld.start()
                }
                SXContract.SENDCODE+"3" -> {
                    isSendNew = true
                    timeNew.start()
                }
                SXContract.UPDATEUSERPHONE -> {
                    val intent = Intent()
                    intent.putExtra("phone",et_new_phone.text.toString())
                    setResult(RESULT_OK,intent)
                    noticeDialog.showNotice(4)
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