package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class QuestionBean (
    @SerializedName("id")val id:String,
    @SerializedName("question")val question:String,
    @SerializedName("answer")val answer:String,
    @SerializedName("optionsList")val optionsList:List<OptionsList>
): Serializable

class OptionsList (
    @SerializedName("id")val id:String,
    @SerializedName("describe")val describe:String,
    @SerializedName("options")val options:String,
    @SerializedName("qid")val qid:String,
    var isSelected:Boolean
): Serializable