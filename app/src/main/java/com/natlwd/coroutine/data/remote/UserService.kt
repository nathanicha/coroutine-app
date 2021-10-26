package com.natlwd.coroutine.data.remote

import com.natlwd.coroutine.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface UserService {
    @GET("/todos/{userId}")
    fun getUser(
        @Path("userId") userId: String
    ): Call<UserResponse?>
}