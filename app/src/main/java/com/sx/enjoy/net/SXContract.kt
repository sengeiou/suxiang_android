package com.sx.enjoy.net

import com.likai.lib.net.HttpResult
import com.sx.enjoy.base.BaseView
import com.sx.enjoy.bean.QuestionBean
import com.sx.enjoy.bean.UserBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Query

interface SXContract {

    companion object {
        var SENDCODE = "sendCode"
        var REGISTER = "register"
        var LOGIN = "login"
        var GETUSERINFO = "getUserInfo"
        var FORGETPASSWORD = "forgetPassword"
        var UPDATEUSERINFO = "updateUserInfo"
        var UPDATEUSERPHONE = "updateUserPhone"
        var UPDATELOGINPASSWORD = "updateLoginPassword"
        var UPDATEPAYPASSWORD = "updatePayPassword"
        var GETSIGNRESULT = "getSignResult"
        var GETQUESTIONLIST = "getQuestionList"
        var USERSIGN = "userSign"
    }

    interface View: BaseView

    interface Model{
        fun sendCode(phone:String,type:String): Observable<HttpResult<String>>
        fun register(code: String,electCode: String,password: String,repNewPassword: String,userPhone:String): Observable<HttpResult<String>>
        fun login(phone:String,password:String):Observable<HttpResult<UserBean>>
        fun getUserInfo(id:String):Observable<HttpResult<UserBean>>
        fun forgetPassword(phone:String,code:String,newPassword:String,repNewPassword:String):Observable<HttpResult<String>>
        fun updateUserInfo(id:String,userImg:String,userName:String,sex:String,email:String,address:String,referralCode:String):Observable<HttpResult<String>>
        fun updateUserPhone(oldPhone:String,oldPhoneCode:String,newPhone:String,newPhoneCode:String):Observable<HttpResult<String>>
        fun updateLoginPassword(userId:String,oldPassword:String,newPassword:String,repNewPassword:String):Observable<HttpResult<String>>
        fun updatePayPassword(userId:String,payPassword:String,newPayPassword:String,repNewPassword:String):Observable<HttpResult<String>>
        fun getSignResult(userId:String):Observable<HttpResult<Boolean>>
        fun getQuestionList():Observable<HttpResult<List<QuestionBean>>>
        fun userSign(userId:String):Observable<HttpResult<String>>
    }

    interface Present{
        fun sendCode(phone:String,type:String)
        fun register(code: String,electCode: String,password: String,repNewPassword: String,userPhone:String)
        fun login(phone:String,password:String)
        fun getUserInfo(id:String)
        fun forgetPassword(phone:String,code:String,newPassword:String,repNewPassword:String)
        fun updateUserInfo(id:String,userImg:String,userName:String,sex:String,email:String,address:String,referralCode:String)
        fun updateUserPhone(oldPhone:String,oldPhoneCode:String,newPhone:String,newPhoneCode:String)
        fun updateLoginPassword(userId:String,oldPassword:String,newPassword:String,repNewPassword:String)
        fun updatePayPassword(userId:String,payPassword:String,newPayPassword:String,repNewPassword:String)
        fun getSignResult(userId:String,isShow:Boolean)
        fun getQuestionList()
        fun userSign(userId:String)
    }

}