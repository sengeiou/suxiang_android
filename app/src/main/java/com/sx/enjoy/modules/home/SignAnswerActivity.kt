package com.sx.enjoy.modules.home

import android.support.v7.widget.GridLayoutManager
import com.sx.enjoy.R
import com.sx.enjoy.adapter.AnswerListAdapter
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.bean.QuestionBean
import com.sx.enjoy.constans.C
import com.sx.enjoy.net.SXContract
import com.sx.enjoy.net.SXPresent
import com.sx.enjoy.utils.ChineseNumToArabicNumUtil
import com.sx.enjoy.view.dialog.AnswerErrorDialog
import com.sx.enjoy.view.dialog.AnswerRightDialog
import kotlinx.android.synthetic.main.activity_sign_answer.*
import org.jetbrains.anko.toast
import java.lang.StringBuilder

class SignAnswerActivity : BaseActivity() ,SXContract.View{

    private lateinit var present: SXPresent

    private lateinit var errorDialog: AnswerErrorDialog
    private lateinit var rightDialog: AnswerRightDialog

    private lateinit var mAdapter : AnswerListAdapter

    private val questList = arrayListOf<QuestionBean>()
    private var questIndex = 0

    override fun getTitleType() = PublicTitleData (C.TITLE_NORMAL,"签到答题")

    override fun getLayoutResource() = R.layout.activity_sign_answer

    override fun beForSetContentView() {
        present = SXPresent(this)
    }

    override fun initView() {
        errorDialog = AnswerErrorDialog(this)
        rightDialog = AnswerRightDialog(this)

        mAdapter = AnswerListAdapter()
        rcy_answer_list.layoutManager = GridLayoutManager(this,3)
        rcy_answer_list.adapter = mAdapter


        initEvent()

        present.getQuestionList()
    }

    private fun initEvent(){
        tv_submit_button.setOnClickListener {
            if(questList.isEmpty()){
                return@setOnClickListener
            }
            if(questIndex>=questList.size){
                present.userSign(C.USER_ID)
                return@setOnClickListener
            }
            if(mAdapter.getSelectItem().isEmpty()){
                toast("请选择答案")
                return@setOnClickListener
            }
            if(questList[questIndex].answer == mAdapter.getSelectItem()){
                questIndex++
                initQuestion()
            }else{
                errorDialog.show()
            }
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            mAdapter.selectItem(position)
        }
        errorDialog.setOnDismissListener {
            mAdapter.selectItem(-1)
        }
        rightDialog.setOnNoticeConfirmListener(object :AnswerRightDialog.OnNoticeConfirmListener{
            override fun onConfirm() {
                finish()
            }
        })
    }


    private fun initQuestion(){
        if(questIndex<questList.size){
            tv_question_number.text = "第${ChineseNumToArabicNumUtil.arabicNumToChineseNum(questIndex+1)}题"
            tv_question.text = questList[questIndex].question
            val sb = StringBuilder()
            questList[questIndex].optionsList.forEach {
                sb.append(it.options+" : "+it.describe+"\n\n")
            }
            tv_operation.text = sb.toString()
            mAdapter.setNewData(questList[questIndex].optionsList)
            if(questIndex<questList.size-1){
                tv_submit_button.text = "下一题"
            }else{
                tv_submit_button.text = "提交"
            }
        }
    }

    override fun onSuccess(flag: String?, data: Any?) {
        flag?.let {
            when (flag) {
                SXContract.GETQUESTIONLIST -> {
                    data?.let {
                        data as List<QuestionBean>
                        questList.clear()
                        questList.addAll(data)
                        questIndex = 0
                        initQuestion()
                    }
                }
                SXContract.USERSIGN -> {
                    rightDialog.show()
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