package com.sx.enjoy.net

import com.likai.lib.base.BasePresent
import com.likai.lib.net.BaseObserver
import com.likai.lib.net.HttpResult
import com.likai.lib.rx.RxSchedulersHelper
import com.sx.enjoy.bean.UserBean

class SXPresent(baseView: SXContract.View) : BasePresent<SXContract.View>(baseView) ,SXContract.Present{

    private val model = SXModel()

    override fun sendCode(phone:String,type:String) {
        model.sendCode(phone,type)
            .compose(RxSchedulersHelper.io_main())
            .subscribe(object : BaseObserver<String>(mContext,true){
                override fun onSuccess(t: HttpResult<String>?) {
                    view.onSuccess(SXContract.SENDCODE,t?.data)
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
}