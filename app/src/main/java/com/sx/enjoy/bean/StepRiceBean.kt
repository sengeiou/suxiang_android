package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StepRiceBean(
    @SerializedName("minStep")val minStep:String,
    @SerializedName("walkRiceGrains")val walkRiceGrains:String,
    @SerializedName("drivingRiceGrains")val drivingRiceGrains:String,
    @SerializedName("rotateMinStep")val rotateMinStep:Int
): Serializable