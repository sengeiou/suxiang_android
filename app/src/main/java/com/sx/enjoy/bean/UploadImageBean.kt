package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UploadImageBean (
    @SerializedName("id")val id:String,
    @SerializedName("name")val name:String,
    @SerializedName("isImg")val isImg:Boolean,
    @SerializedName("width")val width:Double,
    @SerializedName("height")val height:Double,
    @SerializedName("size")val size:Double,
    @SerializedName("url")val url:String
): Serializable