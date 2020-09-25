package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TaskRiceBean (
    @SerializedName("riceGrains")val riceGrains:String,
    @SerializedName("userActivity")val userActivity:String,
    @SerializedName("walkMinStep")val walkMinStep:String,
    @SerializedName("drivingMinStep")val drivingMinStep:String
): Serializable