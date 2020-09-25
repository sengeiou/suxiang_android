package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MarketListBean(
    @SerializedName("id")val id:String,
    @SerializedName("type")val type:Int,
    @SerializedName("createTime")val createTime:String,
    @SerializedName("userId")val userId:String,
    @SerializedName("userName")val userName:String,
    @SerializedName("userImg")val userImg:String,
    @SerializedName("wxQrcode")val wxQrcode:String,
    @SerializedName("aliQrcode")val aliQrcode:String,
    @SerializedName("amount")val amount:String,
    @SerializedName("richNum")val richNum:String,
    @SerializedName("amountSum")val amountSum:Double,
    @SerializedName("wxNumber")val wxNumber:String,
    @SerializedName("aliNumber")val aliNumber:String
): Serializable