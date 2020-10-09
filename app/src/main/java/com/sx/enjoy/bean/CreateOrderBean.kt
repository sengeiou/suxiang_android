package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CreateOrderBean(
    @SerializedName("orderList")val orderList:List<OrderSendBean>
): Serializable