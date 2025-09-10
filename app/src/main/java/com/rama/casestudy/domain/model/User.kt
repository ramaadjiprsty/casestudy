package com.rama.casestudy.domain.model

data class User(
    val id: Int,
    val fullName: String,
    val age: Int,
    val birthDate: String,
    val jobTitle: String,
    val companyName: String,
    val email: String,
    val phone: String,
    val imageUrl: String
)