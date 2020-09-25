package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MarketTransactionListBean(
    @SerializedName("id")val id:String,
    @SerializedName("type")val type:Int,
    @SerializedName("createTime")val createTime:String,
    @SerializedName("userId")val userId:String,
    @SerializedName("userName")val userName:String,
    @SerializedName("headImage")val headImage:String,
    @SerializedName("buyNum")val buyNum:String,
    @SerializedName("buyAmountSum")val buyAmountSum:String,
    @SerializedName("amount")val amount:String,
    @SerializedName("payMethod")val payMethod:Int,
    @SerializedName("payNumber")val payNumber:String,
    @SerializedName("status")val status:Int,
    @SerializedName("aliNumber")val aliNumber:String
): Serializable