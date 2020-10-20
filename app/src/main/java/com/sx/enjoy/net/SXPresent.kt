package com.sx.enjoy.net

import android.util.Log
import com.likai.lib.base.BasePresent
import com.likai.lib.net.BaseObserver
import com.likai.lib.net.HttpResult
import com.likai.lib.rx.RxSchedulersHelper
import com.sx.enjoy.WebDataBean
import com.sx.enjoy.bean.*
import org.jetbrains.anko.startActivity
import java.io.File

class SXPresent(baseView: SXContract.View) : BasePresent<SXContract.View>(baseView) ,SXContract.Present{

    private val model = SXModel()

    override fun sendCode(phone:String,type:String) {
        model.sendCode(phone,type)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    if(type== "2" || type == "3"){
                        view.onSuccess(SXContract.SENDCODE+type,t?.data)
                    }else{
                        view.onSuccess(SXContract.SENDCODE,t?.data)
                    }
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun register(code: String,electCode: String,password: String,repNewPassword: String,userPhone:String) {
        model.register(code,electCode,password,repNewPassword,userPhone)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.REGISTER,t?.data)
                }

                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun login(phone: String, password: String) {
        model.login(phone,password)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<UserBean>(mContext,true){
                override fun onSuccess(t: HttpResult<UserBean>?) {
                    view.onSuccess(SXContract.LOGIN,t?.data)
                }

                override fun onCodeError(t: HttpResult<UserBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun forgetPassword(phone: String, code: String, newPassword: String, repNewPassword: String) {
        model.forgetPassword(phone,code,newPassword,repNewPassword)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.FORGETPASSWORD,t?.data)
                }

                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getUserInfo(id: String) {
        model.getUserInfo(id)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<UserBean>(mContext,false){
                override fun onSuccess(t: HttpResult<UserBean>?) {
                    view.onSuccess(SXContract.GETUSERINFO,t?.data)
                }
                override fun onCodeError(t: HttpResult<UserBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun updateUserInfo(id:String,userImg: String, userName: String, sex: String, email: String, address: String,referralCode:String) {
        model.updateUserInfo(id,userImg,userName,sex,email,address,referralCode)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.UPDATEUSERINFO,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun updateUserPhone(oldPhone: String, oldPhoneCode: String, newPhone: String, newPhoneCode: String) {
        model.updateUserPhone(oldPhone,oldPhoneCode,newPhone,newPhoneCode)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.UPDATEUSERPHONE,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun updateLoginPassword(userId: String, oldPassword: String, newPassword: String, repNewPassword: String) {
        model.updateLoginPassword(userId,oldPassword,newPassword,repNewPassword)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.UPDATELOGINPASSWORD,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun updatePayPassword(userId: String, payPassword:String,newPayPassword:String, repNewPassword: String) {
        model.updatePayPassword(userId,payPassword,newPayPassword,repNewPassword)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.UPDATEPAYPASSWORD,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getSignResult(userId: String,isShow:Boolean) {
        model.getSignResult(userId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<Boolean>(mContext,isShow){
                override fun onSuccess(t: HttpResult<Boolean>?) {
                    view.onSuccess(SXContract.GETSIGNRESULT,t?.data)
                }
                override fun onCodeError(t: HttpResult<Boolean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getQuestionList() {
        model.getQuestionList()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<QuestionBean>>(mContext,true){
                override fun onSuccess(t: HttpResult<List<QuestionBean>>?) {
                    view.onSuccess(SXContract.GETQUESTIONLIST,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<QuestionBean>>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun userSign(userId: String) {
        model.userSign(userId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.USERSIGN,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getTaskList() {
        model.getTaskList()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<TaskListBean>>(mContext,false){
                override fun onSuccess(t: HttpResult<List<TaskListBean>>?) {
                    view.onSuccess(SXContract.GETTASKLIST,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<TaskListBean>>) {
                    view.onFailed(t.message,true)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,true)
                }
            })
    }

    override fun getMyTaskList(userId: String, status: String,page:String,limit:String) {
        model.getMyTaskList(userId,status,page,limit)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<TaskListBean>>(mContext,false){
                override fun onSuccess(t: HttpResult<List<TaskListBean>>?) {
                    view.onSuccess(SXContract.GETMYTASKLIST,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<TaskListBean>>) {
                    view.onFailed(t.message,true)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,true)
                }
            })
    }

    override fun buyTask(userId: String, taskId: String, payPassword: String) {
        model.buyTask(userId,taskId,payPassword)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.BUYTASK,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getTaskRiceGrains(userId: String) {
        model.getTaskRiceGrains(userId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<TaskRiceBean>(mContext,false){
                override fun onSuccess(t: HttpResult<TaskRiceBean>?) {
                    view.onSuccess(SXContract.GETTASKRICEGRAINS,t?.data)
                }
                override fun onCodeError(t: HttpResult<TaskRiceBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getMarketList(pager: String, limit: String) {
        model.getMarketList(pager,limit)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<MarketListBean>>(mContext,false){
                override fun onSuccess(t: HttpResult<List<MarketListBean>>?) {
                    view.onSuccess(SXContract.GETMARKETLIST,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<MarketListBean>>) {
                    view.onFailed(t.message,true)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,true)
                }
            })
    }

    override fun getMarketQuotes(pager:String,limit:String) {
        model.getMarketQuotes(pager,limit)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<MarketQuotesBean>>(mContext,false){
                override fun onSuccess(t: HttpResult<List<MarketQuotesBean>>?) {
                    view.onSuccess(SXContract.GETMARKETQUOTES,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<MarketQuotesBean>>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getMarketDetails(id: String) {
        model.getMarketDetails(id)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<MarketListBean>(mContext,true){
                override fun onSuccess(t: HttpResult<MarketListBean>?) {
                    view.onSuccess(SXContract.GETMARKETDETAILS,t?.data)
                }
                override fun onCodeError(t: HttpResult<MarketListBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getRiceRecordList(userId: String, type: String, pager: String, limit: String) {
        model.getRiceRecordList(userId,type,pager,limit)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<RiceRecordListBean>>(mContext,false){
                override fun onSuccess(t: HttpResult<List<RiceRecordListBean>>?) {
                    view.onSuccess(SXContract.GETRICERECORDLIST,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<RiceRecordListBean>>) {
                    view.onFailed(t.message,true)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,true)
                }
            })
    }

    override fun getStoreCategory(pid: String) {
        model.getStoreCategory(pid)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<StoreCategoryBean>>(mContext,false){
                override fun onSuccess(t: HttpResult<List<StoreCategoryBean>>?) {
                    view.onSuccess(SXContract.GETSTORECATEGORY,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<StoreCategoryBean>>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun uploadFile(imageFile: File): HttpResult<UploadImageBean>? {
        return model.uploadFile(imageFile).execute().body()
    }

    override fun getMyMarketOrderList(userId: String, type: String, status: String, pager: String, limit: String) {
        model.getMyMarketOrderList(userId,type,status,pager,limit)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<MarketTransactionListBean>>(mContext,false){
                override fun onSuccess(t: HttpResult<List<MarketTransactionListBean>>?) {
                    view.onSuccess(SXContract.GETMYMARKETORDERLIST,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<MarketTransactionListBean>>) {
                    view.onFailed(t.message,true)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,true)
                }
            })
    }

    override fun getRiceFromStep(userId: String, deviceId: String, latitude: String, longitude: String, minStep: String, rotateMinStep: String) {
        model.getRiceFromStep(userId,deviceId,latitude,longitude,minStep,rotateMinStep)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<StepRiceBean>(mContext,false){
                override fun onSuccess(t: HttpResult<StepRiceBean>?) {
                    view.onSuccess(SXContract.GETRICEFROMSTEP,t?.data)
                }
                override fun onCodeError(t: HttpResult<StepRiceBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun publishMarketInfo(userId: String, type: String, amount: String, richNum: String, alipayNumber: String) {
        model.publishMarketInfo(userId,type,amount,richNum,alipayNumber)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.PUBLISHMARKETINFO,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun createMarketOrder(userId: String, type: String, amount: String, buyNum: String, alipayNumber: String, orderNo: String) {
        model.createMarketOrder(userId,type,amount,buyNum,alipayNumber,orderNo)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.CREATEMARKETORDER,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getTransactionOrderDetails(id: String) {
        model.getTransactionOrderDetails(id)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<TransactionOrderBean>(mContext,true){
                override fun onSuccess(t: HttpResult<TransactionOrderBean>?) {
                    view.onSuccess(SXContract.GETTRANSACTIONORDERDETAILS,t?.data)
                }
                override fun onCodeError(t: HttpResult<TransactionOrderBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun payMarketOrder(orderNo: String, transaction: String) {
        model.payMarketOrder(orderNo,transaction)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.PAYMARKETORDER,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun confirmMarketOrder(userId: String, richUserId: String, buyNum: String, orderNo: String, type: String) {
        model.confirmMarketOrder(userId,richUserId,buyNum,orderNo,type)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.CONFIRMMARKETORDER,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getUserTeamList(id: String, limit: String, page: String) {
        model.getUserTeamList(id,limit,page)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<TeamListBean>>(mContext,false){
                override fun onSuccess(t: HttpResult<List<TeamListBean>>?) {
                    view.onSuccess(SXContract.GETUSERTEAMLIST,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<TeamListBean>>) {
                    view.onFailed(t.message,true)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,true)
                }
            })
    }

    override fun getWalkHistory(userId: String, m: String, limit: String, page: String) {
        model.getWalkHistory(userId,m,limit,page)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<WalkHistoryBean>>(mContext,false){
                override fun onSuccess(t: HttpResult<List<WalkHistoryBean>>?) {
                    view.onSuccess(SXContract.GETWALKHISTORY,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<WalkHistoryBean>>) {
                    view.onFailed(t.message,true)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,true)
                }
            })
    }

    override fun getHomeBanner() {
        model.getHomeBanner()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<BannerListBean>(mContext,false){
                override fun onSuccess(t: HttpResult<BannerListBean>?) {
                    view.onSuccess(SXContract.GETHOMEBANNER,t?.data)
                }
                override fun onCodeError(t: HttpResult<BannerListBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getHomeNotice() {
        model.getHomeNotice()
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<NoticeListBean>>(mContext,false){
                override fun onSuccess(t: HttpResult<List<NoticeListBean>>?) {
                    view.onSuccess(SXContract.GETHOMENOTICE,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<NoticeListBean>>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getHomeNews(limit: String, page: String) {
        model.getHomeNews(limit,page)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<NewsListBean>>(mContext,false){
                override fun onSuccess(t: HttpResult<List<NewsListBean>>?) {
                    view.onSuccess(SXContract.GETHOMENEWS,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<NewsListBean>>) {
                    view.onFailed(t.message,true)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,true)
                }
            })
    }

    override fun getCommodityList(goodsCateId: String, goodsName: String, sale: String, amount: String, limit: String, page: String) {
        model.getCommodityList(goodsCateId,goodsName,sale,amount,limit,page)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<CommodityListBean>>(mContext,false){
                override fun onSuccess(t: HttpResult<List<CommodityListBean>>?) {
                    view.onSuccess(SXContract.GETCOMMODITYLIST,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<CommodityListBean>>) {
                    view.onFailed(t.message,true)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,true)
                }
            })
    }

    override fun getCommodityDetails(id: String) {
        model.getCommodityDetails(id)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<CommodityDetailsBean>(mContext,true){
                override fun onSuccess(t: HttpResult<CommodityDetailsBean>?) {
                    view.onSuccess(SXContract.GETCOMMODITYDETAILS,t?.data)
                }
                override fun onCodeError(t: HttpResult<CommodityDetailsBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getCommodityLikeList(categoryId: String, goodsId: String) {
        model.getCommodityLikeList(categoryId,goodsId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<CommodityListBean>>(mContext,true){
                override fun onSuccess(t: HttpResult<List<CommodityListBean>>?) {
                    view.onSuccess(SXContract.GETCOMMODITYLIKELIST,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<CommodityListBean>>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getShopCartCount(userId: String) {
        model.getShopCartCount(userId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.GETSHOPCARTCOUNT,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun addShopCart(userId: String, goodsId: String, constituteId: String, goodsNumber: String) {
        model.addShopCart(userId,goodsId,constituteId,goodsNumber)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.ADDSHOPCART,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun createOrder(userId: String, addressId: String, remark: String, ordersDtoList: List<OrderSendBean>) {
        model.createOrder(userId,addressId,remark,ordersDtoList)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<NewOrderBean>(mContext,true){
                override fun onSuccess(t: HttpResult<NewOrderBean>?) {
                    view.onSuccess(SXContract.CREATEORDER,t?.data)
                }
                override fun onCodeError(t: HttpResult<NewOrderBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getFirstAddress(userId: String) {
        model.getFirstAddress(userId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<AddressBean>(mContext,true){
                override fun onSuccess(t: HttpResult<AddressBean>?) {
                    view.onSuccess(SXContract.GETFIRSTADDRESS,t?.data)
                }
                override fun onCodeError(t: HttpResult<AddressBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun saveAddress(userId: String, addressId:String, receiverAddress: String, receiverName: String, receiverPhone: String, province: String, city: String, area: String, isDefault: String) {
        model.saveAddress(userId,addressId,receiverAddress,receiverName,receiverPhone,province,city,area,isDefault)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.SAVEADDRESS,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getMyAddressList(userId: String) {
        model.getMyAddressList(userId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<AddressBean>>(mContext,false){
                override fun onSuccess(t: HttpResult<List<AddressBean>>?) {
                    view.onSuccess(SXContract.GETMYADDRESSLIST,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<AddressBean>>) {
                    view.onFailed(t.message,true)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,true)
                }
            })
    }

    override fun getMyShopCart(userId: String) {
        model.getMyShopCart(userId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<ShopCartBean>>(mContext,false){
                override fun onSuccess(t: HttpResult<List<ShopCartBean>>?) {
                    view.onSuccess(SXContract.GETMYSHOPCART,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<ShopCartBean>>) {
                    view.onFailed(t.message,true)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,true)
                }
            })
    }

    override fun addCommodityNumber(id: String, goodsNumber: String,position:Int,type:Int) {
        model.addCommodityNumber(id,goodsNumber)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.ADDCOMMODITYNUMBER+position+"-"+type,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun deleteCommodityFromShopCart(ids: List<String>) {
        model.deleteCommodityFromShopCart(ids)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.DELETECOMMODITYFROMSHOPCART,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getMyOrderStatusCount(userId: String) {
        model.getMyOrderStatusCount(userId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<OrderStatusCountBean>(mContext,false){
                override fun onSuccess(t: HttpResult<OrderStatusCountBean>?) {
                    view.onSuccess(SXContract.GETMYORDERSTATUSCOUNT,t?.data)
                }
                override fun onCodeError(t: HttpResult<OrderStatusCountBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getOrderList(userId: String, status: String, page: String, limit: String,isShow:Boolean) {
        model.getOrderList(userId,status,page,limit)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<List<OrderListBean>>(mContext,isShow){
                override fun onSuccess(t: HttpResult<List<OrderListBean>>?) {
                    view.onSuccess(SXContract.GETORDERLIST,t?.data)
                }
                override fun onCodeError(t: HttpResult<List<OrderListBean>>) {
                    view.onFailed(t.message,true)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,true)
                }
            })
    }

    override fun cancelOrder(orderId: String) {
        model.cancelOrder(orderId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.CANCELORDER,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getOrderDetails(orderNo: String) {
        model.getOrderDetails(orderNo)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<OrderDetailsBean>(mContext,true){
                override fun onSuccess(t: HttpResult<OrderDetailsBean>?) {
                    view.onSuccess(SXContract.GETORDERDETAILS,t?.data)
                }
                override fun onCodeError(t: HttpResult<OrderDetailsBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun deleteOrder(orderId: String) {
        model.deleteOrder(orderId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.DELETEORDER,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun authUser(userId: String, idNumber: String, name: String, phone: String,payMethod:String) {
        model.authUser(userId,idNumber,name,phone,payMethod)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<PayResultBean>(mContext,true){
                override fun onSuccess(t: HttpResult<PayResultBean>?) {
                    view.onSuccess(SXContract.AUTHUSER,t?.data)
                }
                override fun onCodeError(t: HttpResult<PayResultBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getAuthUser(userId: String) {
        model.getAuthUser(userId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<AuthUserBean>(mContext,true){
                override fun onSuccess(t: HttpResult<AuthUserBean>?) {
                    view.onSuccess(SXContract.GETAUTHUSER,t?.data)
                }
                override fun onCodeError(t: HttpResult<AuthUserBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun orderPay(orderNo: String, payMethod: String, payType: String) {
        model.orderPay(orderNo,payMethod,payType)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<PayResultBean>(mContext,true){
                override fun onSuccess(t: HttpResult<PayResultBean>?) {
                    view.onSuccess(SXContract.ORDERPAY,t?.data)
                }
                override fun onCodeError(t: HttpResult<PayResultBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getNoticeDetails(id: String) {
        model.getNoticeDetails(id)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<NoticeDetailsBean>(mContext,true){
                override fun onSuccess(t: HttpResult<NoticeDetailsBean>?) {
                    view.onSuccess(SXContract.GETNOTICEDETAILS,t?.data)
                }
                override fun onCodeError(t: HttpResult<NoticeDetailsBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun userFeedback(advice: String, images: String, type: String) {
        model.userFeedback(advice,images,type)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.USERFEEDBACK,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getWebData(type:String) {
        model.getWebData(type)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<WebDataBean>(mContext,true){
                override fun onSuccess(t: HttpResult<WebDataBean>?) {
                    view.onSuccess(SXContract.GETWEBDATA,t?.data)
                }
                override fun onCodeError(t: HttpResult<WebDataBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun memberUp(userId: String) {
        model.memberUp(userId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.MEMBERUP,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getMemberInfo(userId: String) {
        model.getMemberInfo(userId)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<MemberUpBean>(mContext,true){
                override fun onSuccess(t: HttpResult<MemberUpBean>?) {
                    view.onSuccess(SXContract.GETMEMBERINFO,t?.data)
                }
                override fun onCodeError(t: HttpResult<MemberUpBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun getNewsDetails(id: String) {
        model.getNewsDetails(id)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<NewsDetailsBean>(mContext,true){
                override fun onSuccess(t: HttpResult<NewsDetailsBean>?) {
                    view.onSuccess(SXContract.GETNEWSDETAILS,t?.data)
                }
                override fun onCodeError(t: HttpResult<NewsDetailsBean>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }

    override fun deleteAddress(id: String) {
        model.deleteAddress(id)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.DELETEADDRESS,t?.data)
                }
                override fun onCodeError(t: HttpResult<String>) {
                    view.onFailed(t.message,false)
                }
                override fun onFailure(e: Throwable?, isNetWorkError: Boolean) {
                    view.onNetError(isNetWorkError,false)
                }
            })
    }
}