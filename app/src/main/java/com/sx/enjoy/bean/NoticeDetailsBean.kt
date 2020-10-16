package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NoticeDetailsBean(
    @SerializedName("content")val content:String,
    @SerializedName("createTime")val createTime:String,
    @SerializedName("title")val title:String
): Serializable