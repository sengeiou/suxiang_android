package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class TeamCountBean(
    @SerializedName("nextTotal")val nextTotal:String,
    @SerializedName("nextValid")val nextValid:String,
    @SerializedName("teamTotal")val teamTotal:String,
    @SerializedName("teamValid")val teamValid:String
): Serializable