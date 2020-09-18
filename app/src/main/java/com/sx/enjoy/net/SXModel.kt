package com.sx.enjoy.net

import com.likai.lib.net.HttpResult
import com.sx.enjoy.bean.UserBean
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject

class SXModel  : SXContract.Model{

    override fun sendCode(phone: String, type: String): Observable<HttpResult<String>> {
        val keyMap = HashMap<String,String>()
        keyMap["phone"] = phone
        keyMap["type"] = type
        return Api.getDefault().sendCode(keyMap)
    }


    override fun register(code: String,electCode: String,password: String,repNewPassword: String,userPhone:String): Observable<HttpResult<String>> {
        val mapValue = mapOf("code" to code,"electCode" to electCode,"password" to password,"repNewPassword" to repNewPassword,"userPhone" to userPhone)
        return Api.getDefault().register(mapValue)
    }

    override fun login(phone: String, password: String): Observable<HttpResult<UserBean>> {
        val mapValue = mapOf("phone" to phone,"password" to password)
        return Api.getDefault().login(mapValue)
    }
}