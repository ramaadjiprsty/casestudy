package com.rama.casestudy.ui.features.user_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rama.casestudy.domain.model.User
import com.rama.casestudy.domain.repository.UserRepository
import com.rama.casestudy.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserDetailViewModel(
    private val repository: UserRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _userDetailState = MutableStateFlow<Resource<User>>(Resource.Loading())
    val userDetailState = _userDetailState.asStateFlow()

    init {
        val userId = savedStateHandle.get<Int>("userId")
        if (userId != null) {
            fetchUserDetail(userId)
        } else {
            _userDetailState.value = Resource.Error("User ID not found")
        }
    }

    private fun fetchUserDetail(id: Int) {
        viewModelScope.launch {
            _userDetailState.value = Resource.Loading()
            val result = repository.getUserById(id)
            _userDetailState.value = result
        }
    }
}