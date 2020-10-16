package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OrderDetailsBean(
    @SerializedName("id")val id:String,
    @SerializedName("orderNo")val orderNo:String,
    @SerializedName("province")val province:String,
    @SerializedName("city")val city:String,
    @SerializedName("area")val area:String,
    @SerializedName("receiverAddress")val receiverAddress:String,
    @SerializedName("receiverName")val receiverName:String,
    @SerializedName("receiverPhone")val receiverPhone:String,
    @SerializedName("createTime")val createTime:String,
    @SerializedName("status")val status:Int,
    @SerializedName("total")val total:String,
    @SerializedName("remark")val remark:String,
    @SerializedName("statusStr")val statusStr:String,
    @SerializedName("storeInfoList")val storeInfoList:List<StoreList>
): Serializable

class StoreList(
    @SerializedName("storeId")val storeId:String,
    @SerializedName("storeName")val storeName:String,
    @SerializedName("orderVoList")val orderVoList:List<CommodityList>
): Serializable

class CommodityList(
    @SerializedName("constitute")val constitute:String,
    @SerializedName("goodsAmount")val goodsAmount:String,
    @SerializedName("goodsId")val goodsId:String,
    @SerializedName("goodsName")val goodsName:String,
    @SerializedName("image")val image:String,
    @SerializedName("purchaseNum")val purchaseNum:String
): Serializable