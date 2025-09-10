package com.rama.casestudy.data.repository

import com.rama.casestudy.data.mapper.toUser
import com.rama.casestudy.data.remote.retrofit.ApiService
import com.rama.casestudy.domain.model.User
import com.rama.casestudy.domain.repository.UserRepository
import com.rama.casestudy.util.Resource
import java.net.UnknownHostException

class UserRepositoryImpl(
    private val api: ApiService
) : UserRepository {
    override suspend fun getUsers(limit: Int, skip: Int): Resource<List<User>> {
        return try {
            val response = api.getUsers(limit, skip)
            val users = response.users.map { it.toUser() }
            Resource.Success(users)
        } catch (e: Exception) {

            if (e is UnknownHostException) {
                Resource.Error("No internet connection. Please check your network and try again.")
            } else {
                Resource.Error("An unknown error occurred: ${e.localizedMessage}")
            }
        }
    }

    // Lakukan hal yang sama untuk getUserById
    override suspend fun getUserById(id: Int): Resource<User> {
        return try {
            val userDto = api.getUserById(id)
            Resource.Success(userDto.toUser())
        } catch (e: Exception) {
            if (e is UnknownHostException) {
                Resource.Error("No internet connection. Please check your network and try again.")
            } else {
                Resource.Error("Failed to fetch user details: ${e.localizedMessage}")
            }
        }
    }
}