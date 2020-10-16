package com.sx.enjoy.net

import com.likai.lib.net.HttpResult
import com.sx.enjoy.WebDataBean
import com.sx.enjoy.bean.*
import io.reactivex.Observable
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONArray
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
        keyMap["page"] = page
        keyMap["limit"] = limit
        if(status.isNotEmpty()){
            keyMap["status"] = status
        }
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

    override fun getRiceFromStep(userId: String, deviceId: String, latitude: String, longitude: String, minStep: String, rotateMinStep: String): Observable<HttpResult<StepRiceBean>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", userId)
            jsonObject.put("deviceId", deviceId)
            jsonObject.put("latitude", latitude)
            jsonObject.put("longitude", longitude)
            jsonObject.put("minStep", minStep)
            jsonObject.put("rotateMinStep", rotateMinStep)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().getRiceFromStep(body)
    }

    override fun publishMarketInfo(userId: String, type: String, amount: String, richNum: String, alipayNumber: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", userId)
            jsonObject.put("type", type)
            jsonObject.put("amount", amount)
            jsonObject.put("richNum", richNum)
            if(alipayNumber.isNotEmpty()){
                jsonObject.put("alipayNumber", alipayNumber)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().publishMarketInfo(body)
    }

    override fun createMarketOrder(userId: String, type: String, amount: String, buyNum: String, alipayNumber: String, orderNo: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", userId)
            jsonObject.put("type", type)
            jsonObject.put("amount", amount)
            jsonObject.put("buyNum", buyNum)
            jsonObject.put("orderNo", orderNo)
            if(alipayNumber.isNotEmpty()){
                jsonObject.put("alipayNumber", alipayNumber)
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().createMarketOrder(body)
    }

    override fun getTransactionOrderDetails(id: String): Observable<HttpResult<TransactionOrderBean>> {
        return Api.getDefault().getTransactionOrderDetails(id)
    }

    override fun payMarketOrder(orderNo: String, transaction: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("orderNo", orderNo)
            jsonObject.put("transaction", transaction)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().payMarketOrder(body)
    }

    override fun confirmMarketOrder(userId: String, richUserId: String, buyNum: String, orderNo: String,  type: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", userId)
            jsonObject.put("richUserId", richUserId)
            jsonObject.put("buyNum", buyNum)
            jsonObject.put("orderNo", orderNo)
            jsonObject.put("type", type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().confirmMarketOrder(body)
    }

    override fun getUserTeamList(id: String, limit: String, page: String): Observable<HttpResult<List<TeamListBean>>> {
        val keyMap = HashMap<String,String>()
        keyMap["id"] = id
        keyMap["limit"] = limit
        keyMap["page"] = page
        return Api.getDefault().getUserTeamList(keyMap)
    }

    override fun getWalkHistory(userId: String, model: String, limit: String, page: String): Observable<HttpResult<List<WalkHistoryBean>>> {
        val keyMap = HashMap<String,String>()
        keyMap["userId"] = userId
        keyMap["model"] = model
        keyMap["limit"] = limit
        keyMap["page"] = page
        return Api.getDefault().getWalkHistory(keyMap)
    }

    override fun getHomeBanner(): Observable<HttpResult<BannerListBean>> {
        return Api.getDefault().getHomeBanner()
    }

    override fun getHomeNotice(): Observable<HttpResult<List<NoticeListBean>>> {
        return Api.getDefault().getHomeNotice()
    }

    override fun getHomeNews(limit: String, page: String): Observable<HttpResult<List<NewsListBean>>> {
        val keyMap = HashMap<String,String>()
        keyMap["limit"] = limit
        keyMap["page"] = page
        return Api.getDefault().getHomeNews(keyMap)
    }

    override fun getCommodityList(goodsCateId: String, goodsName: String, sale: String, amount: String, limit: String, page: String): Observable<HttpResult<List<CommodityListBean>>> {
        val keyMap = HashMap<String,String>()
        if(goodsCateId.isNotEmpty()){
            keyMap["goodsCateId"] = goodsCateId
        }
        if(goodsName.isNotEmpty()){
            keyMap["goodsName"] = goodsName
        }
        if(sale != "-1"){
            keyMap["sale"] = sale
        }
        if(amount != "-1"){
            keyMap["amount"] = amount
        }
        keyMap["limit"] = limit
        keyMap["page"] = page
        return Api.getDefault().getCommodityList(keyMap)
    }

    override fun getCommodityDetails(id: String): Observable<HttpResult<CommodityDetailsBean>> {
        return Api.getDefault().getCommodityDetails(id)
    }

    override fun getCommodityLikeList(categoryId: String, goodsId: String): Observable<HttpResult<List<CommodityListBean>>> {
        val keyMap = HashMap<String,String>()
        keyMap["categoryId"] = categoryId
        keyMap["goodsId"] = goodsId
        return Api.getDefault().getCommodityLikeList(keyMap)
    }

    override fun getShopCartCount(userId: String): Observable<HttpResult<String>> {
        return Api.getDefault().getShopCartCount(userId)
    }

    override fun addShopCart(userId: String, goodsId: String, constituteId: String, goodsNumber: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", userId)
            jsonObject.put("goodsId", goodsId)
            jsonObject.put("conId", constituteId)
            jsonObject.put("goodsNumber", goodsNumber)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().addShopCart(body)
    }

    override fun createOrder(userId:String,addressId: String, remark: String, ordersDtoList: List<OrderSendBean>): Observable<HttpResult<NewOrderBean>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", userId)
            jsonObject.put("addressId", addressId)
            if(remark.isNotEmpty()){
                jsonObject.put("remark", remark)
            }
            val jsonArray = JSONArray()
            ordersDtoList.forEach {
                val obj = JSONObject()
                if(it.conId.isNotEmpty()){
                    obj.put("constituteId",it.conId)
                }
                if(it.goodsId.isNotEmpty()){
                    obj.put("goodsId",it.goodsId)
                }
                if(it.number.isNotEmpty()){
                    obj.put("number",it.number)
                }
                if(it.shoppingCarId.isNotEmpty()){
                    obj.put("shoppingCarId",it.shoppingCarId)
                }
                jsonArray.put(obj)
            }
            jsonObject.put("ordersDtoList",jsonArray)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().createOrder(body)
    }

    override fun getFirstAddress(userId: String): Observable<HttpResult<AddressBean>> {
        return Api.getDefault().getFirstAddress(userId)
    }

    override fun saveAddress(userId: String, receiverAddress: String, receiverName: String, receiverPhone: String, province: String, city: String, area: String, isDefault: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", userId)
            jsonObject.put("receiverAddress", receiverAddress)
            jsonObject.put("receiverName", receiverName)
            jsonObject.put("receiverPhone", receiverPhone)
            jsonObject.put("province", province)
            jsonObject.put("city", city)
            jsonObject.put("area", area)
            jsonObject.put("isDefault", isDefault)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().saveAddress(body)
    }

    override fun getMyAddressList(userId: String): Observable<HttpResult<List<AddressBean>>> {
        return Api.getDefault().getMyAddressList(userId)
    }

    override fun getMyShopCart(userId: String): Observable<HttpResult<List<ShopCartBean>>> {
        return Api.getDefault().getMyShopCart(userId)
    }

    override fun addCommodityNumber(id: String, goodsNumber: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("id", id)
            jsonObject.put("goodsNumber", goodsNumber)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().addCommodityNumber(body)
    }

    override fun deleteCommodityFromShopCart(ids: List<String>): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            val jsonArray = JSONArray()
            ids.forEach {
                jsonArray.put(it)
            }
            jsonObject.put("ids",jsonArray)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().deleteCommodityFromShopCart(body)
    }

    override fun getMyOrderStatusCount(userId: String): Observable<HttpResult<OrderStatusCountBean>> {
        return Api.getDefault().getMyOrderStatusCount(userId)
    }

    override fun getOrderList(userId: String, status: String, page: String, limit: String): Observable<HttpResult<List<OrderListBean>>> {
        val keyMap = HashMap<String,String>()
        keyMap["userId"] = userId
        keyMap["page"] = page
        keyMap["limit"] = limit
        if(status != "-1"){
            keyMap["status"] = status
        }
        return Api.getDefault().getOrderList(keyMap)
    }

    override fun cancelOrder(orderId: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("orderId", orderId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().cancelOrder(body)
    }

    override fun getOrderDetails(orderNo: String): Observable<HttpResult<OrderDetailsBean>> {
        return Api.getDefault().getOrderDetails(orderNo)
    }

    override fun deleteOrder(orderId: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("orderId", orderId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().deleteOrder(body)
    }

    override fun authUser(userId: String, idNumber: String, name: String, phone: String,payMethod:String): Observable<HttpResult<PayResultBean>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", userId)
            jsonObject.put("idNumber", idNumber)
            jsonObject.put("name", name)
            jsonObject.put("phone", phone)
            jsonObject.put("payMethod", payMethod)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().authUser(body)
    }

    override fun getAuthUser(userId: String): Observable<HttpResult<AuthUserBean>> {
        return Api.getDefault().getAuthUser(userId)
    }

    override fun orderPay(orderNo: String, payMethod: String, payType: String): Observable<HttpResult<PayResultBean>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("orderNo", orderNo)
            jsonObject.put("payMethod", payMethod)
            jsonObject.put("payType", payType)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().orderPay(body)
    }

    override fun getNoticeDetails(id: String): Observable<HttpResult<NoticeDetailsBean>> {
        return Api.getDefault().getNoticeDetails(id)
    }

    override fun userFeedback(advice: String, images: String, type: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("advice", advice)
            //jsonObject.put("images", images)
            jsonObject.put("type", type)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().userFeedback(body)
    }

    override fun getWebData(type:String): Observable<HttpResult<WebDataBean>> {
        return Api.getDefault().getWebData(type)
    }

    override fun memberUp(userId: String): Observable<HttpResult<String>> {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("userId", userId)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        val json = jsonObject.toString()
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        return Api.getDefault().memberUp(body)
    }

    override fun getMemberInfo(userId: String): Observable<HttpResult<MemberUpBean>> {
        return Api.getDefault().getMemberInfo(userId)
    }

    override fun getNewsDetails(id: String): Observable<HttpResult<NewsDetailsBean>> {
        return Api.getDefault().getNewsDetails(id)
    }
}