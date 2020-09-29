package com.sx.enjoy

import android.util.Log
import com.likai.lib.app.BaseApplication
import com.likai.lib.commonutils.SharedPreferencesUtil
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
import org.litepal.LitePal
import java.text.SimpleDateFormat
import java.util.*
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

        val user = LitePal.findLast(UserBean::class.java)
        if(user!=null){
            C.USER_ID = user.userId
        }

        val localDate = SharedPreferencesUtil.getCommonString(this,"localDate")
        val newDate = SimpleDateFormat("yyyy-MM-dd").format(Date())
        if(localDate != newDate){
            C.USER_STEP = 0
            SharedPreferencesUtil.putCommonString(this,"localDate",newDate)
            SharedPreferencesUtil.putCommonInt(this,"step",0)
        }else{
            C.USER_STEP = SharedPreferencesUtil.getCommonInt(this,"step")
        }

    }

}