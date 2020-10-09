package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OrderStatusCountBean (
    @SerializedName("haveGoodsNum")val haveGoodsNum:Int,
    @SerializedName("notPayNum")val notPayNum:Int,
    @SerializedName("waitGoodsNum")val waitGoodsNum:Int,
    @SerializedName("waitSendGoodsNum")val waitSendGoodsNum:Int
): Serializable