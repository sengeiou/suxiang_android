package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NewMarketListBean (
    @SerializedName("id")val id:String,
    @SerializedName("type")val type:Int,
    @SerializedName("userId")val userId:String,
    @SerializedName("userName")val userName:String,
    @SerializedName("userImg")val userImg:String,
    @SerializedName("amount")val amount:String,
    @SerializedName("userPhone")val userPhone:String,
    @SerializedName("richNum")val richNum:String,
    @SerializedName("amountSum")val amountSum:String,
    @SerializedName("isAliPay")val isAliPay:Int,
    @SerializedName("isWxPay")val isWxPay:Int,
    @SerializedName("usdt")val usdt:String
): Serializable