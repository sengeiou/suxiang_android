package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AddressBean(
    @SerializedName("id")val id:String,
    @SerializedName("isDefault")val isDefault:Int,
    @SerializedName("receiverAddress")val receiverAddress:String,
    @SerializedName("receiverName")val receiverName:String,
    @SerializedName("receiverPhone")val receiverPhone:String,
    @SerializedName("province")val province:String,
    @SerializedName("city")val city:String,
    @SerializedName("area")val area:String
): Serializable