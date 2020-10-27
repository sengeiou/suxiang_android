package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SignResultBean(
    @SerializedName("signJudge")val signJudge:Boolean,
    @SerializedName("taskJudge")val taskJudge:Boolean
): Serializable