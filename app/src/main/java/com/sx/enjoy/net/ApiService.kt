package com.sx.enjoy.net

import com.likai.lib.net.HttpResult
import com.sx.enjoy.bean.QuestionBean
import com.sx.enjoy.bean.UserBean
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    //注册发送验证码
    @GET("api-user/user/code")
    fun sendCode(@QueryMap map:Map<String,String>):Observable<HttpResult<String>>

    //注册
    @POST("api-user/user/register")
    fun register(@Body body : RequestBody):Observable<HttpResult<String>>

    //登录
    @POST("api-user/user/login")
    fun login(@Body body : RequestBody):Observable<HttpResult<UserBean>>

    //忘记密码
    @POST("api-user/user/fgPassword")
    fun forgetPassword(@Body body : RequestBody):Observable<HttpResult<String>>

    //查询用户信息
    @GET("api-user/user/findUser")
    fun getUserInfo(@Query("id")id:String):Observable<HttpResult<UserBean>>

    //修改用户
    @POST("api-user/user/changeUser")
    fun updateUserInfo(@Body body : RequestBody):Observable<HttpResult<String>>

    //修改用户
    @POST("api-user/user/updatePhone")
    fun updateUserPhone(@Body body : RequestBody):Observable<HttpResult<String>>

    //修改登录密码
    @POST("api-user/user/upPassword")
    fun updateLoginPassword(@Body body : RequestBody):Observable<HttpResult<String>>

    //修改支付密码
    @POST("api-user/user/setPayPassword")
    fun updatePayPassword(@Body body : RequestBody):Observable<HttpResult<String>>


    //是否签到
    @GET("api-question/signRecord/judgmentSign")
    fun getSignResult(@Query("userId")userId:String):Observable<HttpResult<Boolean>>

    //题库
    @GET("api-question/findQuestionsRandom")
    fun getQuestionList():Observable<HttpResult<List<QuestionBean>>>

    //签到
    @POST("api-question/signRecord/addSign")
    fun userSign(@Body body : RequestBody):Observable<HttpResult<String>>

}
