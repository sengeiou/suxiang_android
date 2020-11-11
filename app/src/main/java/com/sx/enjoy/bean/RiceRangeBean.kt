package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RiceRangeBean (
    @SerializedName("richPriceMin")val richPriceMin:String,
    @SerializedName("richPriceMax")val richPriceMax:String
): Serializable