package com.example.testpulse

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("title")val title: String,
    @SerializedName("code")val code: String,
    @SerializedName("detail")val detail: String
)