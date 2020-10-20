package com.sx.enjoy.modules.mine

import android.annotation.SuppressLint
import android.view.Gravity
import android.webkit.JavascriptInterface
import android.widget.LinearLayout
import com.likai.lib.commonutils.DensityUtils
import com.likai.lib.commonutils.ScreenUtils
import com.sx.enjoy.R
import com.sx.enjoy.WebDataBean
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.MemberUpBean
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.MemberUpSuccessEvent
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.view.dialog.LevelUpDialog
import com.tencent.smtt.sdk.WebSettings
import kotlinx.android.synthetic.main.activity_member_up.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.litepal.LitePal
import java.lang.reflect.Method

class MemberUpActivity : BaseActivity() , SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var levelDialog : LevelUpDialog

    private lateinit var user : UserBean

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"会员等级")

    override fun getLayoutResource() = R.layout.activity_member_up

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        levelDialog = LevelUpDialog(this)

        user = LitePal.findLast(UserBean::class.java)

        present.getMemberInfo(C.USER_ID)

        initEvent()
    }

    private fun initEvent(){
        tv_level_up.setOnClickListener {
            if(user.membershipLevel<=0&&user.isReai != "1"){
                startActivity<AuthenticationActivity>()
                return@setOnClickListener
            }
            present.memberUp(C.USER_ID)
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETMEMBERINFO -> {
                    data?.let {
                        data as MemberUpBean
                        tv_member_up.text = data.content
                        if(data.isUpgrade){
                            tv_level_up.setBackgroundResource(R.drawable.bg_main_full_2)
                        }else{
                            tv_level_up.isClickable = user.membershipLevel<=0
                            tv_level_up.setBackgroundResource(R.drawable.bg_main_1_full_2)
                        }
                    }
                }
                SXContract.MEMBERUP -> {
                    EventBus.getDefault().post(MemberUpSuccessEvent(1))
                    present.getMemberInfo(C.USER_ID)
                    levelDialog.show()
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