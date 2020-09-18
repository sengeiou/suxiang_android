package com.sx.enjoy.modules.mine

import com.sx.enjoy.R
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.view.dialog.LevelUpDialog
import kotlinx.android.synthetic.main.activity_member_up.*

class MemberUpActivity : BaseActivity() {

    private lateinit var levelDialog : LevelUpDialog

    override fun getTitleType() = PublicTitleData(C.TITLE_NORMAL,"会员等级")

    override fun getLayoutResource() = R.layout.activity_member_up

    override fun initView() {
        levelDialog = LevelUpDialog(this)

        initEvent()
    }

    private fun initEvent(){
        tv_level_up.setOnClickListener {
            levelDialog.show()
        }
    }

}