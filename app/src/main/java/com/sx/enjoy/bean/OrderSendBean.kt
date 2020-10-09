package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OrderSendBean: Serializable{
    @SerializedName("conId")var conId = ""
    @SerializedName("goodsId")var goodsId = ""
    @SerializedName("number")var number = ""
    @SerializedName("shoppingCarId")var shoppingCarId = ""
    @SerializedName("name")var name = ""
    @SerializedName("specName")var specName = ""
    @SerializedName("price")var price = ""
    @SerializedName("image")var image = ""
}