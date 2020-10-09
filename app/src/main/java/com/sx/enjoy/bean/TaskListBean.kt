package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TaskListBean  (
    @SerializedName("id")val id:String,
    @SerializedName("activeValue")val activeValue:String,
    @SerializedName("dayRich")val dayRich:String,
    @SerializedName("effSteps")val effSteps:String,
    @SerializedName("effMileage")val effMileage:String,
    @SerializedName("maxRich")val maxRich:String,
    @SerializedName("rank")val rank:Int,
    @SerializedName("taskEffTime")val taskEffTime:String,
    @SerializedName("taskName")val taskName:String,
    @SerializedName("taskRich")val taskRich:String,
    @SerializedName("exist")val exist:String,
    @SerializedName("orderNo")val orderNo:String,
    @SerializedName("status")val status:Int,
    @SerializedName("surplus")val surplus:String
): Serializable