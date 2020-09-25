package com.sx.enjoy.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class StoreCategoryBean (
    @SerializedName("id")val id:String,
    @SerializedName("cateImage")val cateImage:String,
    @SerializedName("cateName")val cateName:String,
    @SerializedName("categoryVoList")val categoryVoList:List<StoreCategoryChildBean>,
    var isSelected:Boolean
): Serializable

class StoreCategoryChildBean (
    @SerializedName("id")val id:String,
    @SerializedName("cateImage")val cateImage:String,
    @SerializedName("cateName")val cateName:String
): Serializable