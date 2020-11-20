package com.sx.enjoy.net

import com.likai.lib.net.HttpResult
import com.sx.enjoy.WebDataBean
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
        var GETRICEFROMSTEP = "getRiceFromStep"
        var PUBLISHMARKETINFO = "publishMarketInfo"
        var CREATEMARKETORDER = "createMarketOrder"
        var GETTRANSACTIONORDERDETAILS = "getTransactionOrderDetails"
        var PAYMARKETORDER = "payMarketOrder"
        var CONFIRMMARKETORDER = "confirmMarketOrder"
        var GETUSERTEAMLIST = "getUserTeamList"
        var GETWALKHISTORY = "getWalkHistory"
        var GETHOMEBANNER = "getHomeBanner"
        var GETHOMENOTICE = "getHomeNotice"
        var GETHOMENEWS = "getHomeNews"
        var GETCOMMODITYLIST = "getCommodityList"
        var GETCOMMODITYDETAILS = "getCommodityDetails"
        var GETCOMMODITYLIKELIST = "getCommodityLikeList"
        var GETSHOPCARTCOUNT = "getShopCartCount"
        var ADDSHOPCART = "addShopCart"
        var CREATEORDER = "createOrder"
        var GETFIRSTADDRESS = "getFirstAddress"
        var SAVEADDRESS = "saveAddress"
        var GETMYADDRESSLIST = "getMyAddressList"
        var GETMYSHOPCART = "getMyShopCart"
        var ADDCOMMODITYNUMBER = "addCommodityNumber"
        var DELETECOMMODITYFROMSHOPCART = "deleteCommodityFromShopCart"
        var GETMYORDERSTATUSCOUNT = "getMyOrderStatusCount"
        var GETORDERLIST = "getOrderList"
        var CANCELORDER = "cancelOrder"
        var GETORDERDETAILS = "getOrderDetails"
        var DELETEORDER = "deleteOrder"
        var AUTHUSER = "authUser"
        var GETAUTHUSER = "getAuthUser"
        var ORDERPAY = "orderPay"
        var GETNOTICEDETAILS = "getNoticeDetails"
        var USERFEEDBACK = "userFeedback"
        var GETWEBDATA = "getWebData"
        var MEMBERUP = "memberUp"
        var GETMEMBERINFO = "getMemberInfo"
        var GETNEWSDETAILS = "getNewsDetails"
        var DELETEADDRESS = "deleteAddress"
        var GETTEAMUSER = "getTeamUser"
        var GETUPDATEINFO = "getUpdateInfo"
        var GETTRANSACTIONLIMIT = "getTransactionLimit"
        var GETTRANSACTIONPROCESS = "getTransactionProcess"
        var GETRICHFEE = "getRichFee"
        var GETFUTURELEVEL = "getFutureLevel"
        var EXPERTUPGRADE = "expertUpgrade"
        var GETMARKETDEMAND = "getMarketDemand"
        var GETNEWMARKETLIST = "getNewMarketList"
        var GETRICERANGE = "getRiceRange"
        var POSTPAYMETHODINFO = "postPayMethodInfo"
        var DELETEPAYMETHODINFO = "deletePayMethodInfo"
        var GETTEAMUSERCOUNT = "getTeamUserCount"
        var GETCANCELCOUNT = "getCancelCount"
        var EXCHANGEVIP = "exchangeVip"
        var GETMYCONTRIBLIST = "getMyContribList"
        var GETMYACTIVITYLIST = "getMyActivityList"
        var GETMYSUFFERLIST = "getMySufferList"
    }

    interface View: BaseView

    interface Model{
        fun sendCode(phone:String,type:String): Observable<HttpResult<String>>
        fun register(code: String,electCode: String,password: String,repNewPassword: String,userPhone:String): Observable<HttpResult<String>>
        fun login(phone:String,password:String,equipmentId:String,systems:String,tagName:String):Observable<HttpResult<UserBean>>
        fun getUserInfo(id:String):Observable<HttpResult<UserBean>>
        fun forgetPassword(phone:String,code:String,newPassword:String,repNewPassword:String):Observable<HttpResult<String>>
        fun updateUserInfo(id:String,userImg:String,userName:String,sex:String,email:String,address:String,referralCode:String):Observable<HttpResult<String>>
        fun updateUserPhone(oldPhone:String,oldPhoneCode:String,newPhone:String,newPhoneCode:String):Observable<HttpResult<String>>
        fun updateLoginPassword(userId:String,oldPassword:String,newPassword:String,repNewPassword:String):Observable<HttpResult<String>>
        fun updatePayPassword(userId:String,payPassword:String,newPayPassword:String,repNewPassword:String):Observable<HttpResult<String>>
        fun getSignResult(userId:String):Observable<HttpResult<SignResultBean>>
        fun getQuestionList():Observable<HttpResult<List<QuestionBean>>>
        fun userSign(userId:String):Observable<HttpResult<String>>
        fun getTaskList():Observable<HttpResult<List<TaskListBean>>>
        fun getMyTaskList(userId:String,status:String,page:String,limit:String):Observable<HttpResult<List<TaskListBean>>>
        fun buyTask(userId:String,taskId:String,payPassword:String):Observable<HttpResult<String>>
        fun getTaskRiceGrains(userId:String):Observable<HttpResult<TaskRiceBean>>
        fun getMarketList(pager:String,limit:String):Observable<HttpResult<List<MarketListBean>>>
        fun getMarketQuotes(type:String,pager:String,limit:String):Observable<HttpResult<List<MarketQuotesBean>>>
        fun getMarketDetails(id:String):Observable<HttpResult<MarketListBean>>
        fun getRiceRecordList(userId:String,type:String,pager:String,limit:String):Observable<HttpResult<List<RiceRecordListBean>>>
        fun getStoreCategory(pid:String):Observable<HttpResult<List<StoreCategoryBean>>>
        fun uploadFile(imageFile: File): Call<HttpResult<UploadImageBean>>
        fun getMyMarketOrderList(userId:String,type:String,status:String,pager:String,limit:String):Observable<HttpResult<List<MarketTransactionListBean>>>
        fun getRiceFromStep(userId:String,deviceId:String,latitude:String,longitude:String,minStep:String,rotateMinStep:String):Observable<HttpResult<StepRiceBean>>
        fun publishMarketInfo(userId:String,type:String,amount:String,richNum:String,isAliPay:String,isWxPay:String):Observable<HttpResult<String>>
        fun createMarketOrder(userId:String,type:String,amount:String,buyNum:String,richOrderNo:String,orderNo:String,isCancel:String):Observable<HttpResult<String>>
        fun getTransactionOrderDetails(userId:String,richOrderNo:String):Observable<HttpResult<TransactionOrderBean>>
        fun payMarketOrder(orderNo:String,transaction:String):Observable<HttpResult<String>>
        fun confirmMarketOrder(userId:String,richUserId:String,buyNum:String,orderNo:String,type:String):Observable<HttpResult<String>>
        fun getUserTeamList(id:String,limit:String,page:String):Observable<HttpResult<List<TeamListBean>>>
        fun getWalkHistory(userId:String,model:String,limit:String,page:String):Observable<HttpResult<List<WalkHistoryBean>>>
        fun getHomeBanner():Observable<HttpResult<BannerListBean>>
        fun getHomeNotice():Observable<HttpResult<List<NoticeListBean>>>
        fun getHomeNews(limit:String,page:String):Observable<HttpResult<List<NewsListBean>>>
        fun getCommodityList(goodsCateId:String,goodsName:String,sale:String,amount:String,limit:String,page:String):Observable<HttpResult<List<CommodityListBean>>>
        fun getCommodityDetails(id:String):Observable<HttpResult<CommodityDetailsBean>>
        fun getCommodityLikeList(categoryId:String,goodsId:String):Observable<HttpResult<List<CommodityListBean>>>
        fun getShopCartCount(userId:String):Observable<HttpResult<String>>
        fun addShopCart(userId:String,goodsId:String,constituteId:String,goodsNumber:String):Observable<HttpResult<String>>
        fun createOrder(userId:String,addressId:String,remark:String,ordersDtoList:List<OrderSendBean>):Observable<HttpResult<NewOrderBean>>
        fun getFirstAddress(userId:String):Observable<HttpResult<AddressBean>>
        fun saveAddress(userId:String,addressId:String,receiverAddress:String,receiverName:String,receiverPhone:String,province:String,city:String,area:String,isDefault:String):Observable<HttpResult<String>>
        fun getMyAddressList(userId:String):Observable<HttpResult<List<AddressBean>>>
        fun getMyShopCart(userId:String):Observable<HttpResult<List<ShopCartBean>>>
        fun addCommodityNumber(id:String,goodsNumber:String):Observable<HttpResult<String>>
        fun deleteCommodityFromShopCart(ids:List<String>):Observable<HttpResult<String>>
        fun getMyOrderStatusCount(userId:String):Observable<HttpResult<OrderStatusCountBean>>
        fun getOrderList(userId:String,status:String,page:String,limit:String):Observable<HttpResult<List<OrderListBean>>>
        fun cancelOrder(orderId:String):Observable<HttpResult<String>>
        fun getOrderDetails(orderNo:String):Observable<HttpResult<OrderDetailsBean>>
        fun deleteOrder(orderId:String):Observable<HttpResult<String>>
        fun authUser(userId:String,idNumber:String,name:String,phone:String,payMethod:String):Observable<HttpResult<PayResultBean>>
        fun getAuthUser(userId:String):Observable<HttpResult<AuthUserBean>>
        fun orderPay(orderNo:String,payMethod:String,payType:String):Observable<HttpResult<PayResultBean>>
        fun getNoticeDetails(id:String):Observable<HttpResult<NoticeDetailsBean>>
        fun userFeedback(advice:String,images:String,type:String):Observable<HttpResult<String>>
        fun getWebData(type:String):Observable<HttpResult<WebDataBean>>
        fun memberUp(userId:String):Observable<HttpResult<String>>
        fun getMemberInfo(userId:String):Observable<HttpResult<MemberUpBean>>
        fun getNewsDetails(id:String):Observable<HttpResult<NewsDetailsBean>>
        fun deleteAddress(id:String):Observable<HttpResult<String>>
        fun getTeamUser(userId:String):Observable<HttpResult<TeamUserBean>>
        fun getUpdateInfo(version:String,phoneSystem:String):Observable<HttpResult<UpdateInfoBean>>
        fun getTransactionLimit(type:String):Observable<HttpResult<String>>
        fun getTransactionProcess(userId:String):Observable<HttpResult<TransactionProcessBean>>
        fun getRichFee(userId:String):Observable<HttpResult<String>>
        fun getFutureLevel(userId:String):Observable<HttpResult<FutureLevelBean>>
        fun expertUpgrade(userId:String):Observable<HttpResult<String>>
        fun getMarketDemand():Observable<HttpResult<MarketDemandBean>>
        fun getNewMarketList(type:String,userPhone:String,richMin:String,richMax:String,priceMin:String,priceMax:String,payMethod:String,sectionId:String,page:String,limit:String):Observable<HttpResult<List<NewMarketListBean>>>
        fun getRiceRange():Observable<HttpResult<RiceRangeBean>>
        fun postPayMethodInfo(userId:String,type:String,wxPayName:String,wxQrcode:String,aliPayName:String,payQrcode:String,aliNumber:String):Observable<HttpResult<String>>
        fun deletePayMethodInfo(userId:String,type:String):Observable<HttpResult<String>>
        fun getTeamUserCount(userId:String):Observable<HttpResult<TeamCountBean>>
        fun getCancelCount(userId:String):Observable<HttpResult<CancelCountBean>>
        fun exchangeVip(userId:String,payPassword:String):Observable<HttpResult<String>>
        fun getMyContribList(userId:String,limit:String,page:String):Observable<HttpResult<List<UserPropertyBean>>>
        fun getMyActivityList(userId:String,limit:String,page:String):Observable<HttpResult<List<UserPropertyBean>>>
        fun getMySufferList(userId:String,limit:String,page:String):Observable<HttpResult<List<UserPropertyBean>>>
    }

    interface Present{
        fun sendCode(phone:String,type:String)
        fun register(code: String,electCode: String,password: String,repNewPassword: String,userPhone:String)
        fun login(phone:String,password:String,equipmentId:String,systems:String,tagName:String)
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
        fun getMarketQuotes(type:String,pager:String,limit:String)
        fun getMarketDetails(id:String)
        fun getRiceRecordList(userId:String,type:String,pager:String,limit:String)
        fun getStoreCategory(pid:String)
        fun uploadFile(imageFile:File):HttpResult<UploadImageBean>?
        fun getMyMarketOrderList(userId:String,type:String,status:String,pager:String,limit:String)
        fun getRiceFromStep(userId:String,deviceId:String,latitude:String,longitude:String,minStep:String,rotateMinStep:String)
        fun publishMarketInfo(userId:String,type:String,amount:String,richNum:String,isAliPay:String,isWxPay:String)
        fun createMarketOrder(userId:String,type:String,amount:String,buyNum:String,richOrderNo:String,orderNo:String,isCancel:String)
        fun getTransactionOrderDetails(userId:String,richOrderNo:String)
        fun payMarketOrder(orderNo:String,transaction:String)
        fun confirmMarketOrder(userId:String,richUserId:String,buyNum:String,orderNo:String,type:String)
        fun getUserTeamList(id:String,limit:String,page:String)
        fun getWalkHistory(userId:String,model:String,limit:String,page:String)
        fun getHomeBanner()
        fun getHomeNotice()
        fun getHomeNews(limit:String,page:String,isShow:Boolean)
        fun getCommodityList(goodsCateId:String,goodsName:String,sale:String,amount:String,limit:String,page:String)
        fun getCommodityDetails(id:String)
        fun getCommodityLikeList(categoryId:String,goodsId:String)
        fun getShopCartCount(userId:String)
        fun addShopCart(userId:String,goodsId:String,constituteId:String,goodsNumber:String)
        fun createOrder(userId:String,addressId:String,remark:String,ordersDtoList:List<OrderSendBean>)
        fun getFirstAddress(userId:String)
        fun saveAddress(userId:String,addressId:String,receiverAddress:String,receiverName:String,receiverPhone:String,province:String,city:String,area:String,isDefault:String)
        fun getMyAddressList(userId:String)
        fun getMyShopCart(userId:String)
        fun addCommodityNumber(id:String,goodsNumber:String,position:Int,type:Int)
        fun deleteCommodityFromShopCart(ids:List<String>)
        fun getMyOrderStatusCount(userId:String)
        fun getOrderList(userId:String,status:String,page:String,limit:String,isShow:Boolean)
        fun cancelOrder(orderId:String)
        fun getOrderDetails(orderNo:String)
        fun deleteOrder(orderId:String)
        fun authUser(userId:String,idNumber:String,name:String,phone:String,payMethod:String)
        fun getAuthUser(userId:String)
        fun orderPay(orderNo:String,payMethod:String,payType:String)
        fun getNoticeDetails(id:String)
        fun userFeedback(advice:String,images:String,type:String)
        fun getWebData(type:String)
        fun memberUp(userId:String)
        fun getMemberInfo(userId:String)
        fun getNewsDetails(id:String)
        fun deleteAddress(id:String)
        fun getTeamUser(userId:String)
        fun getUpdateInfo(version:String,phoneSystem:String)
        fun getTransactionLimit(type:String)
        fun getTransactionProcess(userId:String)
        fun getRichFee(userId:String)
        fun getFutureLevel(userId:String)
        fun expertUpgrade(userId:String)
        fun getMarketDemand()
        fun getNewMarketList(type:String,userPhone:String,richMin:String,richMax:String,priceMin:String,priceMax:String,payMethod:String,sectionId:String,page:String,limit:String,isShow:Boolean)
        fun getRiceRange()
        fun postPayMethodInfo(userId:String,type:String,wxPayName:String,wxQrcode:String,aliPayName:String,payQrcode:String,aliNumber:String)
        fun deletePayMethodInfo(userId:String,type:String)
        fun getTeamUserCount(userId:String)
        fun getCancelCount(userId:String,isAutoCancel:Boolean)
        fun exchangeVip(userId:String,payPassword:String)
        fun getMyContribList(userId:String,limit:String,page:String)
        fun getMyActivityList(userId:String,limit:String,page:String)
        fun getMySufferList(userId:String,limit:String,page:String)
    }

}