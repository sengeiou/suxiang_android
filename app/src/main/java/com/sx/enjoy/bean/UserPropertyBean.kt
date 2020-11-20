package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserPropertyBean (
    @SerializedName("id")val id:String,
    @SerializedName("typeStr")val typeStr:String,
    @SerializedName("createTime")val createTime:String,
    @SerializedName("contrib")val contrib:String,
    @SerializedName("activity")val activity:String,
    @SerializedName("acqExp")val acqExp:String
): Serializable