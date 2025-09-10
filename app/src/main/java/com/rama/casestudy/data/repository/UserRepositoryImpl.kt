package com.rama.casestudy.data.repository

import android.util.Log
import com.rama.casestudy.data.mapper.toUser
import com.rama.casestudy.data.remote.retrofit.ApiService
import com.rama.casestudy.domain.model.User
import com.rama.casestudy.domain.repository.UserRepository
import com.rama.casestudy.util.Resource


class UserRepositoryImpl(
    private val api: ApiService
) : UserRepository {
    override suspend fun getUsers(limit: Int, skip: Int): Resource<List<User>> {
        Log.d("KoinSetup", ">>> MENCOBA MEMANGGIL api.getUsers di dalam UserRepositoryImpl <<<")

        return try {
            val response = api.getUsers(limit, skip)
            val users = response.users.map { it.toUser() }
            Resource.Success(users)
        } catch (e: Exception) {
            Log.e("KoinSetup", "!!! ERROR di UserRepositoryImpl !!!", e)
            Resource.Error("An unknown error occurred: ${e.localizedMessage}")
        }
    }

    override suspend fun getUserById(id: Int): Resource<User> {
        return try {
            val userDto = api.getUserById(id)
            Resource.Success(userDto.toUser())
        } catch (e: Exception) {
            Resource.Error("Failed to fetch user details: ${e.localizedMessage}")
        }
    }
}