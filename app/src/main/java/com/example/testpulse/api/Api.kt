package com.example.testpulse.api

import android.util.JsonReader
import com.example.testpulse.User
import com.example.testpulse.models.UserRequest
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @Headers("Content-Type: application/json", "Accept:application/json")
    @POST("auth")
    suspend fun authUser(@Body user: User): Response<UserRequest>

}