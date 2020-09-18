package com.sx.enjoy.view.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.sx.enjoy.R
import kotlinx.android.synthetic.main.dialog_level_up.*

class LevelUpDialog : Dialog {

    constructor(context: Context) : super(context, R.style.CustomDialog)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_level_up)

        initEvent()
    }


    private fun initEvent(){
        tv_confirm.setOnClickListener {
            dismiss()
        }
    }

}