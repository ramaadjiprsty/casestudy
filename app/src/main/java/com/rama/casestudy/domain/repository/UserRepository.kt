package com.rama.casestudy.domain.repository

import com.rama.casestudy.domain.model.User
import com.rama.casestudy.util.Resource

interface UserRepository {
    suspend fun getUsers(limit: Int, skip: Int): Resource<List<User>>
    suspend fun getUserById(id: Int): Resource<User>
}