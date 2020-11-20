package com.sx.enjoy.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sx.enjoy.R
import kotlinx.android.synthetic.main.dialog_question_view.*

class QuestionDialog : Dialog {

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_question_view)

        initEvent()
    }

    fun showQuestion(index:Int,content:String){
        show()
        tv_content.text = content
        when(index){
            1 -> {
                tv_title.text = "有效人数"
                tv_ok.text = "知道了"
                tv_ok.setBackgroundResource(R.drawable.bg_red_full_15)
            }
            2 -> {
                tv_title.text = "米粒兑换"
                tv_ok.text = "知道了"
                tv_ok.setBackgroundResource(R.drawable.bg_main_full_15)
            }
        }
    }

    private fun initEvent(){
        rl_close.setOnClickListener {
            dismiss()
        }
        tv_ok.setOnClickListener {
            dismiss()
        }
    }

}