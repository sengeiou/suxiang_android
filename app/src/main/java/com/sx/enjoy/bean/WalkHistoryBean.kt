package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WalkHistoryBean(
    @SerializedName("createTime")val createTime:String,
    @SerializedName("minStep")val minStep:String,
    @SerializedName("riceGrains")val riceGrains:String
): Serializable