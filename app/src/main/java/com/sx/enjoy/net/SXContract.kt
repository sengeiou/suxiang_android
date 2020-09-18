package com.sx.enjoy.net

import com.likai.lib.net.HttpResult
import com.sx.enjoy.base.BaseView
import com.sx.enjoy.bean.UserBean
import io.reactivex.Observable
import retrofit2.http.QueryMap

interface SXContract {

    companion object {
        var SENDCODE = "sendCode"
        var REGISTER = "register"
        var LOGIN = "login"
    }

    interface View: BaseView

    interface Model{
        fun sendCode(phone:String,type:String): Observable<HttpResult<String>>
        fun register(code: String,electCode: String,password: String,repNewPassword: String,userPhone:String): Observable<HttpResult<String>>
        fun login(phone:String,password:String):Observable<HttpResult<UserBean>>
    }

    interface Present{
        fun sendCode(phone:String,type:String)
        fun register(code: String,electCode: String,password: String,repNewPassword: String,userPhone:String)
        fun login(phone:String,password:String)
    }

}