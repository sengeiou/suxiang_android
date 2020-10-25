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
    var riceGrains: Double = 0.0
    var userActivity: Double = 0.0
    var userContrib: Double = 0.0
    var isPayPwd:Int = 0
    var membershipLevel: Int = 0
    var membershipLevelName :String = ""
    var pushId: String = ""
    var isReai: String = ""
    var isLock: String = ""
    var wxName: String = ""
    var wxOpenId: String = ""
    var wxQrcode: String = ""
    var wxUnionId: String = ""
    var cardNo: String = ""
    var payQrcode: String = ""
    var wxNumber: String = ""
    var aliNumber: String = ""
    var email: String = ""
    var referralCode: String = ""
    var userLink: String = ""
}