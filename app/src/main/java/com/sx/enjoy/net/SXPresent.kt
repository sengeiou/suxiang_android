package com.sx.enjoy.net

import android.util.Log
import com.likai.lib.base.BasePresent
import com.likai.lib.net.BaseObserver
import com.likai.lib.net.HttpResult
import com.likai.lib.rx.RxSchedulersHelper
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
                    view.onSuccess(if(isShow)SXContract.GETSIGNRESULT+"1" else SXContract.GETSIGNRESULT+"0",t?.data)
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
}