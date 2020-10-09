package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class NoticeListBean(
    @SerializedName("id")val id:String,
    @SerializedName("title")val title:String
): Serializable