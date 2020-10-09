package com.sx.enjoy

import android.os.Handler
import com.gyf.immersionbar.ImmersionBar
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import org.jetbrains.anko.startActivity

class GuideActivity : BaseActivity() {


    override fun getTitleType() = PublicTitleData (C.TITLE_CUSTOM)

    override fun getLayoutResource() = R.layout.activity_guide

    override fun initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).statusBarView(R.id.guide_view).init()

        Handler().postDelayed({
            startActivity<MainActivity>()
            finish()
        },2000)
    }
}
