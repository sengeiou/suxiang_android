package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName

class TransactionProcessBean(
    @SerializedName("id")val id:String,
    @SerializedName("orderType")val orderType:Int
)