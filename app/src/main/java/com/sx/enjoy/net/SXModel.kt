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


    override fun register(code: String, electCode: String, password: String, repNewPassword: String, userPhone: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("code", code)
            jsonObject.put("electCode", electCode)
            jsonObject.put("password", password)
            jsonObject.put("repNewPassword", repNewPassword)
            jsonObject.put("userPhone", userPhone)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().register(body)
    }

    override fun login(phone: String, password: String): Observable<HttpResult<UserBean>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("phone", phone)
            jsonObject.put("password", password)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().login(body)
    }
}