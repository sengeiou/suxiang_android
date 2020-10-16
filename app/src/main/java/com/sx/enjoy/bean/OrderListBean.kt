package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OrderListBean(
    @SerializedName("id")val id:String,
    @SerializedName("orderNo")val orderNo:String,
    @SerializedName("createTime")val createTime:String,
    @SerializedName("total")val total:String,
    @SerializedName("status")val status:Int,
    @SerializedName("statusStr")val statusStr:String,
    @SerializedName("totalNumber")val totalNumber:String,
    @SerializedName("orderVoList")val orderVoList:List<OrderCommodityList>
): Serializable

class OrderCommodityList(
    @SerializedName("id")val id:String,
    @SerializedName("childOrderNo")val childOrderNo:String,
    @SerializedName("constitute")val constitute:String,
    @SerializedName("goodsAmount")val goodsAmount:String,
    @SerializedName("goodsId")val goodsId:String,
    @SerializedName("goodsName")val goodsName:String,
    @SerializedName("image")val image:String,
    @SerializedName("purchaseNum")val purchaseNum:String
): Serializable