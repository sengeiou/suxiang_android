package com.sx.enjoy

import android.os.Handler
import com.gyf.immersionbar.ImmersionBar
import com.sx.enjoy.base.BaseActivity
import com.sx.enjoy.constans.C
import kotlinx.android.synthetic.main.activity_guide.*
import org.jetbrains.anko.startActivity

class GuideActivity : BaseActivity() {

    //private var isTest = false

    override fun getTitleType() = PublicTitleData (C.TITLE_CUSTOM)

    override fun getLayoutResource() = R.layout.activity_guide

    override fun initView() {
        ImmersionBar.with(this).statusBarDarkFont(true).statusBarView(R.id.guide_view).init()

        Handler().postDelayed({
            startActivity<MainActivity>()
            finish()
        },2000)

//        tv_confirm.setOnClickListener {
//            startActivity<MainActivity>()
//        }
//        tv_address_host.setOnClickListener {
//            isTest = !isTest
//            if(isTest){
//                tv_address_host.text = "测试服"
//                C.HX_BASE_ADDRESS = "http://192.168.1.240:9900/"
//            }else{
//                tv_address_host.text = "正式服"
//                C.HX_BASE_ADDRESS = "http://app.suxiang986.com/"
//            }
//        }
    }
}
