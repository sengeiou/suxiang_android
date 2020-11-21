package com.sx.enjoy

import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import com.sx.enjoy.base.BaseGuideActivity
import com.sx.enjoy.constans.C
import com.sx.enjoy.utils.AppStatus
import com.sx.enjoy.utils.AppStatusManager
import org.jetbrains.anko.startActivity
import com.dm.sdk.ads.splash.SplashAD
import com.dm.sdk.ads.splash.SplashAdListener
import com.dm.sdk.common.util.AdError
import kotlinx.android.synthetic.main.activity_guide.*
import android.view.KeyEvent.KEYCODE_HOME
import android.view.KeyEvent.KEYCODE_BACK




class GuideActivity : BaseGuideActivity(), SplashAdListener {

    //private var splashAD: SplashAD? = null

    override fun getTitleType() = PublicTitleData (C.TITLE_CUSTOM)

    override fun getLayoutResource() = R.layout.activity_guide

    override fun initView() {
//        splashAD = SplashAD(this, C.DOMOB_APP_KEY, C.DOMOB_DEMO_ID, this, 10000)
//        splashAD?.fetchAndShowIn(splash_container)
        Handler().postDelayed({
            AppStatusManager.getInstance().setAppStatus(AppStatus.STATUS_NORMAL)
            startActivity<MainActivity>()
            finish()
        },2000)
    }


    override fun onAdClicked() {
        Log.e("Test","onAdClicked----------->")
    }

    override fun onAdDismissed() {
        Log.e("Test","onAdDismissed----------->")
    }

    override fun onAdPresent() {
        Log.e("Test","onAdPresent----------->")
    }

    override fun onNoAd(p0: AdError?) {
        Log.e("Test","onNoAd----------->"+p0?.errorCode+"-----------"+p0?.errorMsg)

    }

    override fun onAdLoaded(p0: Long) {
        Log.e("Test","onAdLoaded----------->")
    }

    override fun onAdFilled() {
        Log.e("Test","onAdFilled----------->")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KEYCODE_BACK || keyCode == KEYCODE_HOME) {
            true
        } else super.onKeyDown(keyCode, event)
    }

}
