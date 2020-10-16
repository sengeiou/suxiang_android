package com.sx.enjoy

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class WebDataBean(
    @SerializedName("address")val address:String,
    @SerializedName("contact")val contact:String,
    @SerializedName("content")val content:String,
    @SerializedName("phone")val phone:String,
    @SerializedName("type")val type:Int
): Serializable