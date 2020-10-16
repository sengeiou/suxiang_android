package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PayResultBean(
    @SerializedName("aliPay")val aliPay:String
): Serializable