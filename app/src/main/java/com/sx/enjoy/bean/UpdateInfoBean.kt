package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UpdateInfoBean (
    @SerializedName("isUpdate")val isUpdate:Int,
    @SerializedName("isMust")val isMust:Int,
    @SerializedName("downloadUrl")val downloadUrl:String,
    @SerializedName("updateContent")val updateContent:String
): Serializable