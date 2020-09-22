package com.sx.enjoy.net

import com.likai.lib.base.BasePresent
import com.likai.lib.net.BaseObserver
import com.likai.lib.net.HttpResult
import com.likai.lib.rx.RxSchedulersHelper
import com.sx.enjoy.bean.QuestionBean
import com.sx.enjoy.bean.UserBean

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
}