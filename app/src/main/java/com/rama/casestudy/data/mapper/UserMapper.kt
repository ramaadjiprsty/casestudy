package com.rama.casestudy.data.mapper

import com.rama.casestudy.data.remote.dto.UserDto
import com.rama.casestudy.domain.model.User

fun UserDto.toUser(): User {
    return User(
        id = id,
        fullName = "$firstName $lastName",
        age = age,
        birthDate = birthDate,
        jobTitle = company.title,
        companyName = company.name,
        email = email,
        phone = phone,
        imageUrl = image
    )
}