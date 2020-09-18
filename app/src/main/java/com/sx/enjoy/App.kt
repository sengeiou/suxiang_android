package com.sx.enjoy

import android.util.Log
import com.likai.lib.app.BaseApplication
import com.sx.enjoy.bean.UserBean
import com.sx.enjoy.constans.C
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

        val user = LitePal.findLast(UserBean::class.java)
        Log.e("Test","user----------->$user")
        if(user!=null){
            Log.e("Test","id----------->"+user.userId)
            C.USER_ID = user.userId
        }
    }

}