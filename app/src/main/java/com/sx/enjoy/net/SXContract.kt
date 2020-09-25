package com.sx.enjoy.net

import com.likai.lib.net.HttpResult
import com.sx.enjoy.base.BaseView
import com.sx.enjoy.bean.*
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.io.File

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
        var GETTASKLIST = "getTaskList"
        var GETMYTASKLIST = "getMyTaskList"
        var BUYTASK = "buyTask"
        var GETTASKRICEGRAINS = "getTaskRiceGrains"
        var GETMARKETLIST = "getMarketList"
        var GETMARKETQUOTES = "getMarketQuotes"
        var GETMARKETDETAILS = "getMarketDetails"
        var GETRICERECORDLIST = "getRiceRecordList"
        var GETSTORECATEGORY = "getStoreCategory"
        var GETMYMARKETORDERLIST = "getMyMarketOrderList"
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
        fun getTaskList():Observable<HttpResult<List<TaskListBean>>>
        fun getMyTaskList(userId:String,status:String,page:String,limit:String):Observable<HttpResult<List<TaskListBean>>>
        fun buyTask(userId:String,taskId:String,payPassword:String):Observable<HttpResult<String>>
        fun getTaskRiceGrains(userId:String):Observable<HttpResult<TaskRiceBean>>
        fun getMarketList(pager:String,limit:String):Observable<HttpResult<List<MarketListBean>>>
        fun getMarketQuotes(pager:String,limit:String):Observable<HttpResult<List<MarketQuotesBean>>>
        fun getMarketDetails(id:String):Observable<HttpResult<MarketListBean>>
        fun getRiceRecordList(userId:String,type:String,pager:String,limit:String):Observable<HttpResult<List<RiceRecordListBean>>>
        fun getStoreCategory(pid:String):Observable<HttpResult<List<StoreCategoryBean>>>
        fun uploadFile(imageFile: File): Call<HttpResult<UploadImageBean>>
        fun getMyMarketOrderList(userId:String,type:String,status:String,pager:String,limit:String):Observable<HttpResult<List<MarketTransactionListBean>>>
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
        fun getTaskList()
        fun getMyTaskList(userId:String,status:String,page:String,limit:String)
        fun buyTask(userId:String,taskId:String,payPassword:String)
        fun getTaskRiceGrains(userId:String)
        fun getMarketList(pager:String,limit:String)
        fun getMarketQuotes(pager:String,limit:String)
        fun getMarketDetails(id:String)
        fun getRiceRecordList(userId:String,type:String,pager:String,limit:String)
        fun getStoreCategory(pid:String)
        fun uploadFile(imageFile:File):HttpResult<UploadImageBean>?
        fun getMyMarketOrderList(userId:String,type:String,status:String,pager:String,limit:String)
    }

}