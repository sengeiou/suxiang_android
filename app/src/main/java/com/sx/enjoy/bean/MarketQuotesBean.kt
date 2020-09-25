package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MarketQuotesBean (
    @SerializedName("amount")val amount:String,
    @SerializedName("createTime")val createTime:String
): Serializable