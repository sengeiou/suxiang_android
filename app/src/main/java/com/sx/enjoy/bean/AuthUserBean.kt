package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AuthUserBean(
    @SerializedName("id")val id:String,
    @SerializedName("idNumber")val idNumber:String,
    @SerializedName("name")val name:String,
    @SerializedName("phone")val phone:String,
    @SerializedName("status")val status:Int,
    @SerializedName("userId")val userId:String,
    @SerializedName("price")val price:String,
    @SerializedName("orderNo")val orderNo:String
): Serializable