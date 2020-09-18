package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import org.litepal.crud.LitePalSupport
import java.io.Serializable

class UserBean (
    @SerializedName("id")val id:Long,
    @SerializedName("userId")var userId:String,
    @SerializedName("address")val address:String,
    @SerializedName("aliNumber")val aliNumber:String,
    @SerializedName("cardNo")val cardNo:String,
    @SerializedName("createTime")val createTime:String,
    @SerializedName("email")val email:String,
    @SerializedName("isDel")val isDel:Int,
    @SerializedName("isLock")val isLock:Int,
    @SerializedName("isReai")val isReai:Int,
    @SerializedName("membershipLevel")val membershipLevel:Int,
    @SerializedName("password")val password:String,
    @SerializedName("payPassword")val payPassword:String,
    @SerializedName("payQrcode")val payQrcode:String,
    @SerializedName("pushId")val pushId:String,
    @SerializedName("riceGrains")val riceGrains:Int,
    @SerializedName("sex")val sex:String,
    @SerializedName("updateTime")val updateTime:String,
    @SerializedName("userActivity")val userActivity:Int,
    @SerializedName("userContrib")val userContrib:Int,
    @SerializedName("userImg")val userImg:String,
    @SerializedName("userLevel")val userLevel:Int,
    @SerializedName("userLevelName")val userLevelName:String,
    @SerializedName("userName")val userName:String,
    @SerializedName("userPhone")val userPhone:String,
    @SerializedName("wxName")val wxName:String,
    @SerializedName("wxNumber")val wxNumber:String,
    @SerializedName("wxOpenId")val wxOpenId:String,
    @SerializedName("wxQrcode")val wxQrcode:String,
    @SerializedName("wxUnionId")val wxUnionId:String
): LitePalSupport(), Serializable