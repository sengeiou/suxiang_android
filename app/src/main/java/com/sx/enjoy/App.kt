package com.sx.enjoy

import android.util.Log
import cn.jpush.android.api.JPushInterface
import com.likai.lib.app.BaseApplication
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import com.tencent.smtt.sdk.QbSdk
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import org.litepal.LitePal
import kotlin.properties.Delegates


class App: BaseApplication(){

    override fun onCreate() {
        super.onCreate()
        init()
    }

    companion object {
        var instance : App by Delegates.notNull()
    }

    private fun init(){
        instance = this
        LitePal.initialize(this)
        LitePal.getDatabase()

        QbSdk.setDownloadWithoutWifi(true)
        QbSdk.initX5Environment(this, object : QbSdk.PreInitCallback {
            override fun onCoreInitFinished() {}
            override fun onViewInitFinished(b: Boolean) {
                Log.e("Test", "X5--------->complete--->$b")
            }
        })

        UMConfigure.init(this,C.U_MENG_APP_KEY, "Umeng",UMConfigure.DEVICE_TYPE_PHONE, null)
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
        UMConfigure.setLogEnabled(false)

        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)

        val user = LitePal.findLast(UserBean::class.java)
        if(user!=null){
            C.USER_ID = user.userId
        }
    }

}