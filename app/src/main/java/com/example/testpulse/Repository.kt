package com.example.testpulse

import com.example.testpulse.api.RetrofitInstance

class Repository {
    suspend fun postUserAuth(user: User) = RetrofitInstance.api.authUser(user)
}