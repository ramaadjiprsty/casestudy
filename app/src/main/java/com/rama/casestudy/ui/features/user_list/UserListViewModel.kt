package com.rama.casestudy.ui.features.user_list

import com.rama.casestudy.util.Resource

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rama.casestudy.domain.model.User
import com.rama.casestudy.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserListViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _userListState = MutableStateFlow<Resource<List<User>>>(Resource.Loading())
    val userListState = _userListState.asStateFlow()

    init {
        fetchUsers()
    }

    fun fetchUsers(limit: Int = 30, skip: Int = 0) {
        viewModelScope.launch {
            _userListState.value = Resource.Loading()
            val result = repository.getUsers(limit, skip)
            _userListState.value = result
        }
    }
}