package com.rama.casestudy.data.remote.retrofit

import com.rama.casestudy.data.remote.dto.UserListResponseDto
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {
    @GET("users")
    suspend fun getUsers(
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): UserListResponseDto
}