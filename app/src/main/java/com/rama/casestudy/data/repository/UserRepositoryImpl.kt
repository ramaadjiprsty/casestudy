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
        // Log baru tepat sebelum pemanggilan API
        Log.d("KoinSetup", ">>> MENCOBA MEMANGGIL api.getUsers di dalam UserRepositoryImpl <<<")

        return try {
            val response = api.getUsers(limit, skip)
            val users = response.users.map { it.toUser() }
            Resource.Success(users)
        } catch (e: Exception) {
            // Log error yang lebih detail, dengan menyertakan 'e'
            Log.e("KoinSetup", "!!! ERROR di UserRepositoryImpl !!!", e)
            Resource.Error("An unknown error occurred: ${e.localizedMessage}")
        }
    }
}