package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MarketDemandBean(
    @SerializedName("demandSum")val demandSum:String,
    @SerializedName("transactionSum")val transactionSum:String,
    @SerializedName("todayRichQuotes")val todayRichQuotes:String,
    @SerializedName("usdt")val usdt:String,
    @SerializedName("sections")val sections :List<SectionList>
): Serializable

class SectionList(
    @SerializedName("sectionId")val sectionId:String,
    @SerializedName("sectionStr")val sectionStr:String
): Serializable