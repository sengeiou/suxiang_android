package com.sx.enjoy.net

import com.likai.lib.net.HttpResult
import com.sx.enjoy.bean.*
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import java.io.File

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

    override fun forgetPassword(phone: String, code: String, newPassword: String, repNewPassword: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("phone", phone)
            jsonObject.put("code", code)
            jsonObject.put("newPassword", newPassword)
            jsonObject.put("repNewPassword", repNewPassword)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().forgetPassword(body)
    }

    override fun getUserInfo(id: String): Observable<HttpResult<UserBean>> {
        return Api.getDefault().getUserInfo(id)
    }

    override fun updateUserInfo(id:String,userImg: String, userName: String, sex: String, email: String, address: String,referralCode:String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("id", id)
            jsonObject.put("userImg", userImg)
            jsonObject.put("userName", userName)
            jsonObject.put("sex", sex)
            jsonObject.put("email", email)
            jsonObject.put("address", address)
            if(referralCode.isNotEmpty()){
                jsonObject.put("referralCode", referralCode)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().updateUserInfo(body)
    }

    override fun updateUserPhone(oldPhone: String, oldPhoneCode: String, newPhone: String, newPhoneCode: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("oldPhone", oldPhone)
            jsonObject.put("oldPhoneCode", oldPhoneCode)
            jsonObject.put("newPhone", newPhone)
            jsonObject.put("newPhoneCode", newPhoneCode)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().updateUserPhone(body)
    }

    override fun updateLoginPassword(userId: String, oldPassword: String, newPassword: String, repNewPassword: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", userId)
            jsonObject.put("oldPassword", oldPassword)
            jsonObject.put("newPassword", newPassword)
            jsonObject.put("repNewPassword", repNewPassword)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().updateLoginPassword(body)
    }

    override fun updatePayPassword(userId: String, payPassword:String,newPayPassword:String, repNewPassword: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", userId)
            jsonObject.put("payPassword", payPassword)
            jsonObject.put("newPayPassword", newPayPassword)
            jsonObject.put("repNewPassword", repNewPassword)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().updatePayPassword(body)
    }

    override fun getSignResult(userId: String): Observable<HttpResult<Boolean>> {
        return Api.getDefault().getSignResult(userId)
    }

    override fun getQuestionList(): Observable<HttpResult<List<QuestionBean>>> {
        return Api.getDefault().getQuestionList()
    }

    override fun userSign(userId: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", userId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().userSign(body)
    }

    override fun getTaskList(): Observable<HttpResult<List<TaskListBean>>> {
        return Api.getDefault().getTaskList()
    }

    override fun getMyTaskList(userId: String, status: String,page:String,limit:String): Observable<HttpResult<List<TaskListBean>>> {
        val keyMap = HashMap<String,String>()
        keyMap["userId"] = userId
        keyMap["status"] = status
        keyMap["page"] = page
        keyMap["limit"] = limit
        return Api.getDefault().getMyTaskList(keyMap)
    }

    override fun buyTask(userId: String, taskId: String, payPassword: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", userId)
            jsonObject.put("taskId", taskId)
            jsonObject.put("payPassword", payPassword)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().buyTask(body)
    }

    override fun getTaskRiceGrains(userId: String): Observable<HttpResult<TaskRiceBean>> {
        return Api.getDefault().getTaskRiceGrains(userId)
    }

    override fun getMarketList(pager: String, limit: String): Observable<HttpResult<List<MarketListBean>>> {
        val keyMap = HashMap<String,String>()
        keyMap["page"] = pager
        keyMap["limit"] = limit
        return Api.getDefault().getMarketList(keyMap)
    }

    override fun getMarketQuotes(pager:String,limit:String): Observable<HttpResult<List<MarketQuotesBean>>> {
        val keyMap = HashMap<String,String>()
        keyMap["page"] = pager
        keyMap["limit"] = limit
        return Api.getDefault().getMarketQuotes(keyMap)
    }

    override fun getMarketDetails(id: String): Observable<HttpResult<MarketListBean>> {
        return Api.getDefault().getMarketDetails(id)
    }

    override fun getRiceRecordList(userId: String, type: String, pager: String, limit: String): Observable<HttpResult<List<RiceRecordListBean>>> {
        val keyMap = HashMap<String,String>()
        keyMap["userId"] = userId
        keyMap["type"] = type
        keyMap["page"] = pager
        keyMap["limit"] = limit
        return Api.getDefault().getRiceRecordList(keyMap)
    }

    override fun getStoreCategory(pid: String): Observable<HttpResult<List<StoreCategoryBean>>> {
        return Api.getDefault().getStoreCategory(pid)
    }

    override fun uploadFile(imageFile: File): Call<HttpResult<UploadImageBean>> {
        val requestParam = MultipartBody.Builder().setType(MultipartBody.FORM)
        val image = imageFile.name.split(".")
        requestParam.addFormDataPart("file", imageFile.name, RequestBody.create(MediaType.parse("image/${image[1]}"), imageFile))
        return Api.getDefault().uploadFile(requestParam.build() as RequestBody)
    }

    override fun getMyMarketOrderList(userId: String, type: String, status: String, pager: String, limit: String): Observable<HttpResult<List<MarketTransactionListBean>>> {
        val keyMap = HashMap<String,String>()
        keyMap["userId"] = userId
        keyMap["type"] = type
        keyMap["status"] = status
        keyMap["page"] = pager
        keyMap["limit"] = limit
        return Api.getDefault().getMyMarketOrderList(keyMap)
    }
}