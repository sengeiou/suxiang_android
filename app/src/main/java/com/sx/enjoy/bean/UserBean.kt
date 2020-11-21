package com.sx.enjoy.bean

import org.litepal.crud.LitePalSupport

class UserBean: LitePalSupport(){
    var id: Long = 0
    var userId: String = ""
    var userName: String = ""
    var userPhone: String = ""
    var userImg: String = ""
    var userLevelName: String = ""
    var sex: String = ""
    var address: String = ""
    var riceGrain: String = ""
    var userActivity: Double = 0.0
    var userContrib: Double = 0.0
    var experience : Int = 0
    var isPayPwd:Int = 0
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

    fun setActivity(userActivity:Double){
        if(userActivity != 0.0){
            this.userActivity = userActivity
        }else{
            this.setToDefault("userActivity")
        }
    }

    fun setContrib(userContrib:Double){
        if(userContrib != 0.0){
            this.userContrib = userContrib
        }else{
            this.setToDefault("userContrib")
        }
    }

    fun setUserExperience(experience:Int){
        if(experience != 0){
            this.experience = experience
        }else{
            this.setToDefault("experience")
        }
    }

    fun setIsPayPwd(isPayPwd:Int){
        if(isPayPwd != 0){
            this.isPayPwd = isPayPwd
        }else{
            this.setToDefault("isPayPwd")
        }
    }
}