package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NewsDetailsBean(
    @SerializedName("id")val id:String,
    @SerializedName("createTime")val createTime:String,
    @SerializedName("title")val title:String,
    @SerializedName("content")val content:String,
    @SerializedName("img")val img:String,
    @SerializedName("summary")val summary:String
): Serializable