package com.sx.enjoy.modules.mine

import android.view.Gravity
import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.view.dialog.NoticeDialog
import kotlinx.android.synthetic.main.activity_feedback.*
import org.jetbrains.anko.toast

class FeedbackActivity : BaseActivity() ,SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var noticeDialog :NoticeDialog

    private var feedbackType = -1

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"问题反馈")

    override fun getLayoutResource() = R.layout.activity_feedback

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        noticeDialog = NoticeDialog(this)


        ll_question_1.setOnClickListener {
            tb_question_1.isChecked = true
            tb_question_2.isChecked = false
            feedbackType = 0
        }
        ll_question_2.setOnClickListener {
            tb_question_1.isChecked = false
            tb_question_2.isChecked = true
            feedbackType = 1
        }
        tv_submit.setOnClickListener {
            if(!tb_question_1.isChecked&&!tb_question_2.isChecked){
                toast("请选择反馈问题的类型").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            if(et_feedback_reason.text.isEmpty()){
                toast("请输入您的宝贵意见").setGravity(Gravity.CENTER, 0, 0)
                return@setOnClickListener
            }
            present.userFeedback(et_feedback_reason.text.toString(),"",feedbackType.toString())
        }
        noticeDialog.setOnDismissListener {
            finish()
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.USERFEEDBACK -> {
                    noticeDialog.showNotice(10)
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