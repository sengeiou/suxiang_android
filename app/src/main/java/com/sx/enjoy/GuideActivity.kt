package com.sx.enjoy

import android.os.Handler
import com.sx.enjoy.base.BaseGuideActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.utils.AppStatus
import com.sx.enjoy.utils.AppStatusManager
import org.jetbrains.anko.startActivity


class GuideActivity : BaseGuideActivity(){

    override fun getTitleType() = PublicTitleData (C.TITLE_CUSTOM)

    override fun getLayoutResource() = R.layout.activity_guide

    override fun initView() {
        Handler().postDelayed({
            AppStatusManager.getInstance().setAppStatus(AppStatus.STATUS_NORMAL)
            startActivity<MainActivity>()
            finish()
        },2000)
    }

}
