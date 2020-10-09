package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TeamListBean(
    @SerializedName("id")val id:String,
    @SerializedName("createTime")val createTime:String,
    @SerializedName("userActivity")val userActivity:String,
    @SerializedName("userContrib")val userContrib:String,
    @SerializedName("userImg")val userImg:String,
    @SerializedName("userName")val userName:String
): Serializable