package com.app.flobiz.model.dataclass

import com.google.gson.annotations.SerializedName

data class StackOverflow(
    @SerializedName("items") val items : List<Items>,
    @SerializedName("has_more") val has_more : Boolean,
    @SerializedName("quota_max") val quota_max : Int,
    @SerializedName("quota_remaining") val quota_remaining : Int
)
