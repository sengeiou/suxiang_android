package com.sx.enjoy.bean

import org.litepal.crud.LitePalSupport

class UserBean: LitePalSupport(){
    var id: Long = 0
    var userId: String = ""
    var isDel: Int = 0
    var userName: String = ""
    var userPhone: String = ""
    var userImg: String = ""
    var userLevel: String = ""
    var userLevelName: String = ""
    var sex: String = ""
    var address: String = ""
    var riceGrains: Int = 0
    var userActivity: Int = 0
    var userContrib: Int = 0
    var membershipLevel: String = ""
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
}