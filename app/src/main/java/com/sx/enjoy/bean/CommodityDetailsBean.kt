package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CommodityDetailsBean(
    @SerializedName("id")val id:String,
    @SerializedName("amount")val amount:String,
    @SerializedName("repertory")val repertory:String,
    @SerializedName("assembleAmount")val assembleAmount:String,
    @SerializedName("goodsCateId")val goodsCateId:String,
    @SerializedName("goodsName")val goodsName:String,
    @SerializedName("pointOrigin")val pointOrigin:String,
    @SerializedName("image")val image:String,
    @SerializedName("firstImage")val firstImage:String,
    @SerializedName("sale")val sale:String,
    @SerializedName("imageDetail")val imageDetail:String,
    @SerializedName("isAssemble")val isAssemble:String,
    @SerializedName("isSeckill")val isSeckill:String,
    @SerializedName("postFee")val postFee:String,
    @SerializedName("seckillAmount")val seckillAmount:String,
    @SerializedName("seckillTime")val seckillTime:String,
    @SerializedName("status")val status:String,
    @SerializedName("storeId")val storeId:String,
    @SerializedName("sxConstituteList")val sxConstituteList:List<ConstitueList>,
    @SerializedName("specificationVoList")val specificationVoList:List<SpecList>
): Serializable


class ConstitueList(
    @SerializedName("id")val id:String,
    @SerializedName("constitute")val constitute:String,
    @SerializedName("createTime")val createTime:String,
    @SerializedName("goodsId")val goodsId:String,
    @SerializedName("price")val price:String,
    @SerializedName("repertory")val repertory:String,
    @SerializedName("specId")val specId:String
): Serializable

class SpecList(
    @SerializedName("id")val id:String,
    @SerializedName("image")val image:String,
    @SerializedName("paramName")val paramName:String,
    @SerializedName("isImage")val isImage:Boolean,
    @SerializedName("specificationVoList")val specificationVoList:List<SpecChildListBean>
): Serializable

class SpecChildListBean(
    @SerializedName("id")val id:String,
    @SerializedName("image")val image:String,
    @SerializedName("paramName")val paramName:String,
    @SerializedName("isStock")val isStock:Boolean,
    var isSelected:Boolean
): Serializable