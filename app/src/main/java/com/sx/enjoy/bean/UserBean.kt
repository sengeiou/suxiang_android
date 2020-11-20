package com.sx.enjoy.bean

import org.litepal.crud.LitePalSupport

class UserBean: LitePalSupport(){
    var id: Long = 0
    var userId: String = ""
    var userName: String = ""
    var userPhone: String = ""
    var userImg: String = ""
    var userLevel: Int = 0
    var userLevelName: String = ""
    var sex: String = ""
    var address: String = ""
    var riceGrain: String = ""
    var userActivity: Double = 0.0
    var userContrib: Double = 0.0
    var experience : Int = 0
    var isPayPwd:Int = 0
    var membershipLevel: Int = 0
    var membershipLevelName :String = ""
    var isReai: String = ""
    var wxNumber: String = ""
    var email: String = ""
    var referralCode: String = ""
    var userLink: String = ""
    var isWxPay: Boolean = false
    var isAliPay: Boolean = false
    var aliNumber : String = ""
    var aliPayName : String = ""
    var payQrcode : String = ""
    var wxPayName : String = ""
    var wxQrcode : String = ""

    fun setIsWxPay(isWxPay:Boolean){
        if(isWxPay){
            this.isWxPay = isWxPay
        }else{
            this.setToDefault("isWxPay")
        }
    }

    fun setIsAliPay(isAliPay:Boolean){
        if(isAliPay){
            this.isAliPay = isAliPay
        }else{
            this.setToDefault("isAliPay")
        }
    }
}