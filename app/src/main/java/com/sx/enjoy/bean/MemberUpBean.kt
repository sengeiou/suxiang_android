package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MemberUpBean(
    @SerializedName("id")val id:String,
    @SerializedName("activity")val activity:String,
    @SerializedName("level")val level:String,
    @SerializedName("number")val number:String,
    @SerializedName("poundage")val poundage:String,
    @SerializedName("rank")val rank:String,
    @SerializedName("riceGrains")val riceGrains:String,
    @SerializedName("suffer")val suffer:String,
    @SerializedName("userSuffer")val userSuffer:String,
    @SerializedName("isUpgrade")val isUpgrade:Boolean,
    @SerializedName("content")val content:String,
    @SerializedName("upgradeAskList")val upgradeAskList:List<UpgradeAskList>
): Serializable

class UpgradeAskList(
    @SerializedName("levelFirst")val levelFirst:String,
    @SerializedName("levelSecond")val levelSecond:String,
    @SerializedName("numberFirst")val numberFirst:String,
    @SerializedName("numberSecond")val numberSecond:String,
    @SerializedName("type")val type:Int,
    @SerializedName("userNumberSecond")val userNumberSecond:String,
    @SerializedName("userNumberFirst")val userNumberFirst:String
): Serializable
