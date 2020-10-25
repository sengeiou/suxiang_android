package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TeamUserBean(
    @SerializedName("id")val id:String,
    @SerializedName("userActivity")val userActivity:String,
    @SerializedName("createTime")val createTime:String,
    @SerializedName("userImg")val userImg:String,
    @SerializedName("userLevel")val userLevel:String,
    @SerializedName("userName")val userName:String,
    @SerializedName("userPhone")val userPhone:String
): Serializable