package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class FutureLevelBean(
    @SerializedName("id")val id:String,
    @SerializedName("content")val content:String,
    @SerializedName("isUpgrade")val isUpgrade:Boolean
): Serializable