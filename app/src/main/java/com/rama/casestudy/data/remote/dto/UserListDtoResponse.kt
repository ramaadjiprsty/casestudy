package com.rama.casestudy.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserListResponseDto(
    val users: List<UserDto>,
    val total: Int,
    val skip: Int,
    val limit: Int
)