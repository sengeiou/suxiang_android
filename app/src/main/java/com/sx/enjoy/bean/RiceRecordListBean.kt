package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RiceRecordListBean (
    @SerializedName("id")val id:String,
    @SerializedName("createTime")val createTime:String,
    @SerializedName("isIncome")val isIncome:Int,
    @SerializedName("rich")val rich:String,
    @SerializedName("type")val type:Int,
    @SerializedName("richFee")val richFee:String,
    @SerializedName("typeStr")val typeStr:String
): Serializable