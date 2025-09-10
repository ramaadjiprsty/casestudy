package com.rama.casestudy.data.remote.dto
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val birthDate: String,
    val email: String,
    val phone: String,
    val image: String,
    val company: CompanyDto
)

@Serializable
data class CompanyDto(
    val title: String,
    val name: String
)