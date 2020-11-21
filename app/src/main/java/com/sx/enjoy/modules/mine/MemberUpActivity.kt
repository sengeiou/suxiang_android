package com.sx.enjoy.modules.mine

import android.text.Html
import android.view.Gravity
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.MemberUpBean
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.event.MemberUpSuccessEvent
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.EncryptionUtil
import com.sx.enjoy.view.dialog.*
import kotlinx.android.synthetic.main.activity_member_up.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.litepal.LitePal

class MemberUpActivity : BaseActivity() , SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var levelDialog : LevelUpDialog
    private lateinit var questionDialog: QuestionDialog
    private lateinit var payDialog: PayPasswordDialog
    private lateinit var reminderDialog: ReminderDialog
    private lateinit var vipChangeDialog: VIPChangeDialog

    private var user : UserBean? = null
    private var member : MemberUpBean? = null

    private var isLevelUp = false

    override fun getTitleType() = PublicTitleData(C.TITLE_CUSTOM)

    override fun getLayoutResource() = R.layout.activity_member_up

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(member_up_title).init()

        levelDialog = LevelUpDialog(this)
        questionDialog = QuestionDialog(this)
        payDialog = PayPasswordDialog(this)
        reminderDialog = ReminderDialog(this)
        vipChangeDialog = VIPChangeDialog(this)

        user = LitePal.findLast(UserBean::class.java)

        present.getMemberInfo(C.USER_ID)

        initEvent()
    }

    private fun initEvent(){
        ll_member_back.setOnClickListener {
            finish()
        }
        tv_level_up.setOnClickListener {
            if(isLevelUp){
                present.memberUp(C.USER_ID)
            }
        }
        iv_change_vip.setOnClickListener {
            questionDialog.showQuestion(2,member!!.exchangeExplain)
        }
        tv_change_vip.setOnClickListener {
            if(user?.isPayPwd == 1){
                if(member!!.useNumber != member!!.maxNumber){
                    vipChangeDialog.showVIPChange(member!!.vipBuyRich,member!!.useNumber,member!!.maxNumber)
                }else{
                    toast("本周兑换VIP次数已满").setGravity(Gravity.CENTER, 0, 0)
                }
            }else{
                reminderDialog.showReminder(2)
            }
        }
        iv_network_error.setOnClickListener {
            present.getMemberInfo(C.USER_ID)
        }
        tv_auth_result.setOnClickListener {
            if(user!!.isReai != "1"){
                startActivity<AuthenticationActivity>()
            }
        }

        reminderDialog.setOnNoticeConfirmListener(object :ReminderDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                startActivity<PayPasswordActivity>()
            }
        })
        payDialog.setOnNoticeConfirmListener(object :PayPasswordDialog.OnNoticeConfirmListener{
            override fun onConfirm(password: String) {
                present.exchangeVip(C.USER_ID, EncryptionUtil.MD5(password))
            }
        })
        vipChangeDialog.setOnNoticeConfirmListener(object :VIPChangeDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                payDialog.showInputPassword()
            }
        })
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETMEMBERINFO -> {
                    data?.let {
                        data as MemberUpBean
                        rl_error_view.visibility = View.GONE
                        member = data
                        if(data.userLevel == data.level){
                            ll_vip_level.text = "VIP等级"+data.userLevel
                            tv_charge_level.text = "当前手续费：${data.userPoundage}%"
                            ll_next_get.visibility = View.GONE
                            ll_up_view.visibility = View.GONE
                            tv_level_up.visibility = View.GONE
                        }else{
                            ll_next_get.visibility = View.VISIBLE
                            ll_up_view.visibility = View.VISIBLE
                            tv_level_up.visibility = View.VISIBLE
                            ll_vip_level.text = "VIP等级"+data.userLevel
                            tv_charge_level.text = "当前手续费：${data.userPoundage}%"
                            tv_vip_next.text = "下一等级 VIP${data.level}奖励"
                            if(data.number > 0){
                                ll_get_task.visibility = View.VISIBLE
                                tv_task_get.text = "${if(data.rank == "0") "初" else data.rank}级卷轴x${data.number}"
                            }else{
                                ll_get_task.visibility = View.GONE
                            }
                            if(data.activity > 0){
                                ll_get_activity.visibility = View.VISIBLE
                                tv_activity_get.text = "活跃度${String.format("%.2f",data.activity)}"
                            }else{
                                ll_get_activity.visibility = View.GONE
                            }
                            if(data.riceGrains > 0){
                                ll_get_rice.visibility = View.VISIBLE
                                tv_rice_get.text = "米粒${String.format("%.2f",data.riceGrains)}"
                            }else{
                                ll_get_rice.visibility = View.GONE
                            }
                            if(data.poundage > 0){
                                ll_get_charge.visibility = View.VISIBLE
                                tv_charge_get.text = "手续费${data.poundage}%"
                            }else{
                                ll_get_charge.visibility = View.GONE
                            }

                            if(data.userLevel>0){
                                ll_auth_view.visibility = View.GONE
                                if(data.suffer>0){
                                    ll_exp_view.visibility = View.VISIBLE
                                    tv_user_cur_exp.text = data.userSuffer
                                    tv_user_total_exp.text = "/"+data.suffer
                                    tv_exp_get_way.text = data.sufferAccess
                                }else{
                                    ll_exp_view.visibility = View.GONE
                                }
                                if(data.scrollNumber>0){
                                    ll_task_view.visibility = View.VISIBLE
                                    tv_task_target.text = if(data.scrollRequire == 0) "初" else data.scrollRequire.toString() + "级卷轴"
                                    tv_user_cur_task.text = data.userScrollNumber
                                    tv_user_total_task.text = "/"+data.scrollNumber
                                    tv_task_get_way.text = data.taskAccess
                                }else{
                                    ll_task_view.visibility = View.GONE
                                }
                                if(data.upgradeAskVoList.isNotEmpty()){
                                    ll_team_view.visibility = View.VISIBLE
                                    val sb = StringBuffer()
                                    for(i in data.upgradeAskVoList.indices){
                                        for (j in data.upgradeAskVoList[i].upgradeAskList.indices){
                                            sb.append("<font color='#F36F4A'>${data.upgradeAskVoList[i].upgradeAskList[j].userNumberFirst}</font>/${data.upgradeAskVoList[i].upgradeAskList[j].numberFirst} v${data.upgradeAskVoList[i].upgradeAskList[j].levelFirst} ")
                                            if(j<data.upgradeAskVoList[i].upgradeAskList.size-1){
                                                sb.append("<font color='#666666'>且 </font>")
                                            }
                                        }
                                        if(i<data.upgradeAskVoList.size-1){
                                            sb.append("<font color='#666666'>或</font><br/>")
                                        }
                                    }
                                    tv_team_value.text = Html.fromHtml(sb.toString())
                                    tv_team_get_way.text = data.teamAccess
                                }else{
                                    ll_team_view.visibility = View.GONE
                                }
                            }else{
                                ll_auth_view.visibility = View.VISIBLE
                                ll_exp_view.visibility = View.GONE
                                ll_task_view.visibility = View.GONE
                                ll_team_view.visibility = View.GONE

                                tv_auth_result.text = if(user!!.isReai == "1") "已实名" else "去实名"
                            }

                            isLevelUp = data.isUpgrade
                            tv_level_up.setBackgroundResource(if(data.isUpgrade) R.drawable.bg_main_full_2 else R.drawable.bg_main_1_full_2)
                        }
                    }
                }
                SXContract.MEMBERUP -> {
                    EventBus.getDefault().post(MemberUpSuccessEvent(1))
                    present.getMemberInfo(C.USER_ID)
                    levelDialog.show()
                }
                SXContract.EXCHANGEVIP -> {
                    toast("兑换成功").setGravity(Gravity.CENTER, 0, 0)
                    present.getMemberInfo(C.USER_ID)
                }
                else -> {

                }
            }
        }
    }


    override fun onFailed(string: String?,isRefreshList:Boolean) {
        if(isRefreshList){
            rl_error_view.visibility = View.GONE
        }
        toast(string!!).setGravity(Gravity.CENTER, 0, 0)
    }

    override fun onNetError(boolean: Boolean,isRefreshList:Boolean) {
        if(isRefreshList){
            rl_error_view.visibility = View.VISIBLE
        }else{
            toast("请检查网络连接").setGravity(Gravity.CENTER, 0, 0)
        }
    }

}