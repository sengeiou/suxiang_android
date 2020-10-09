package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CommodityListBean(
    @SerializedName("id")val id:String,
    @SerializedName("amount")val amount:String,
    @SerializedName("assembleAmount")val assembleAmount:String,
    @SerializedName("goodsCateId")val goodsCateId:String,
    @SerializedName("goodsName")val goodsName:String,
    @SerializedName("firstImage")val firstImage:String,
    @SerializedName("isAssemble")val isAssemble:String,
    @SerializedName("isSeckill")val isSeckill:String,
    @SerializedName("postFee")val postFee:String,
    @SerializedName("seckillAmount")val seckillAmount:Double,
    @SerializedName("seckillTime")val seckillTime:String,
    @SerializedName("status")val status:String,
    @SerializedName("storeId")val storeId:String
): Serializable