package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class BannerListBean(
    @SerializedName("advertList")val advertList:List<AdvertListBean>,
    @SerializedName("shuffList")val shuffList:List<ShuffListBean>
): Serializable

class AdvertListBean(
    @SerializedName("id")val id:String,
    @SerializedName("bannerImg")val bannerImg:String,
    @SerializedName("type")val type:Int,
    @SerializedName("url")val url:String,
    @SerializedName("heigth")val heigth:String,
    @SerializedName("width")val width:String
)

class ShuffListBean(
    @SerializedName("id")val id:String,
    @SerializedName("bannerImg")val bannerImg:String,
    @SerializedName("type")val type:Int,
    @SerializedName("url")val url:String,
    @SerializedName("heigth")val heigth:String,
    @SerializedName("width")val width:String
)