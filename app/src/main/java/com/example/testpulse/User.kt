package com.example.testpulse

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login") var login: String,
    @SerializedName("password") var password: String
)