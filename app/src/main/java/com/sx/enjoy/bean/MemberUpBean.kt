package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MemberUpBean(
    @SerializedName("id")val id:String,
    @SerializedName("activity")val activity:Double,
    @SerializedName("heigestLevel")val heigestLevel:String,
    @SerializedName("isUpgrade")val isUpgrade:Boolean,
    @SerializedName("level")val level:String,
    @SerializedName("maxNumber")val maxNumber:String,
    @SerializedName("number")val number:Int,
    @SerializedName("poundage")val poundage:Int,
    @SerializedName("rank")val rank:String,
    @SerializedName("riceGrains")val riceGrains:Double,
    @SerializedName("scrollNumber")val scrollNumber:Int,
    @SerializedName("scrollRequire")val scrollRequire:Int,
    @SerializedName("suffer")val suffer:Int,
    @SerializedName("useNumber")val useNumber:String,
    @SerializedName("userLevel")val userLevel:Int,
    @SerializedName("userPoundage")val userPoundage:String,
    @SerializedName("userScrollNumber")val userScrollNumber:String,
    @SerializedName("userSuffer")val userSuffer:String,
    @SerializedName("vipBuyRich")val vipBuyRich:String,
    @SerializedName("exchangeExplain")val exchangeExplain:String,
    @SerializedName("sufferAccess")val sufferAccess:String,
    @SerializedName("taskAccess")val taskAccess:String,
    @SerializedName("teamAccess")val teamAccess:String,
    @SerializedName("upgradeAskVoList")val upgradeAskVoList:List<UpgradeAskVoList>,
    @SerializedName("vipExchangeList")val vipExchangeList:List<VipExchangeList>
): Serializable

class UpgradeAskVoList(
    @SerializedName("condition")val condition:Int,
    @SerializedName("upgradeAskList")val upgradeAskList:List<UpgradeAskList>
)

class UpgradeAskList(
    @SerializedName("id")val id:String,
    @SerializedName("levelFirst")val levelFirst:String,
    @SerializedName("numberFirst")val numberFirst:String,
    @SerializedName("userNumberFirst")val userNumberFirst:String
): Serializable

class VipExchangeList(
    @SerializedName("id")val id:String,
    @SerializedName("vipBuyRich")val vipBuyRich:String,
    @SerializedName("vipGrade")val vipGrade:String,
    @SerializedName("vipMax")val vipMax:String,
    @SerializedName("vipMin")val vipMin:String
)
