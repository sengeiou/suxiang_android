package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CancelCountBean(
    @SerializedName("cancelNum")val cancelNum:Int,
    @SerializedName("systemCancelNum")val systemCancelNum:Int,
    @SerializedName("isAutoCancel")var isAutoCancel:Boolean
): Serializable