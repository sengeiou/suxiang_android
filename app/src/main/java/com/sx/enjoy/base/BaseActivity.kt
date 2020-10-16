package com.sx.enjoy.base

import android.os.Bundle
import com.gyf.immersionbar.ImmersionBar
import com.likai.lib.base.MyBaseActivity
import com.likai.lib.manager.TokenLoseEfficacy
import com.sx.enjoy.R
import com.sx.enjoy.constans.C
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.title_public_view.*


abstract class BaseActivity : MyBaseActivity()  , TokenLoseEfficacy.OnTokenLoseEfficacyListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initTitle()
    }

    private fun initTitle(){
        val d = getTitleType()
        if(d.type == C.TITLE_CUSTOM){

            return
        }
        ImmersionBar.with(this).statusBarDarkFont(true).titleBar(public_title).init()
        when(d.type){
            C.TITLE_NORMAL -> {
                tv_public_title.text = d.title
            }
            C.TITLE_RIGHT_TEXT -> {
                tv_public_title.text = d.title
                tv_public_right.text = d.right
                tv_public_right.setTextColor(resources.getColor(d.rightTextColor))
            }
        }
        ll_public_back.setOnClickListener {
            finish()
        }
    }

    abstract fun getTitleType():PublicTitleData

    class PublicTitleData{
        var type:Int
        var title:String
        var right:String
        var rightImg:Int
        var rightTextColor:Int

        constructor(type:Int):this(type,"")

        constructor(type:Int,title:String):this(type,title,"")

        constructor(type:Int,title:String,right:String):this(type,title,right,0)

        constructor(type:Int,title:String,right:String,rightImg:Int):this(type,title,right,rightImg,R.color.main_color)

        constructor(type:Int,title:String,right:String,rightImg:Int,rightTextColor:Int){
            this.type = type
            this.title = title
            this.right = right
            this.rightImg = rightImg
            this.rightTextColor = rightTextColor
        }

    }

    override fun onResume() {
        super.onResume()
        TokenLoseEfficacy.getInstance().setOnTokenLoseEfficacyListener(this)
        MobclickAgent.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        MobclickAgent.onPause(this)
    }


    override fun onTokenLoseEfficacy() {
//        if(this is MainActivity){
//            this.showPager(0)
//            this.reLoadPagerData()
//        }else{
//            EventBus.getDefault().post(ComeHomeEvent(0))
//            startActivity<MainActivity>()
//        }
    }

}
