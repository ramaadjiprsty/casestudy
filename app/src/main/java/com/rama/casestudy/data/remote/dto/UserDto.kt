package com.rama.casestudy.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val age: Int, // <-- Tambahkan ini
    val email: String,
    val phone: String,
    val image: String,
    val company: CompanyDto // <-- Tambahkan ini
)

@Serializable
data class CompanyDto(
    val title: String
)