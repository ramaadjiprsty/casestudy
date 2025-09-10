package com.rama.casestudy.ui.features.user_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rama.casestudy.domain.model.User
import com.rama.casestudy.domain.repository.UserRepository
import com.rama.casestudy.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Definisikan enum di sini
enum class SortOrder {
    NONE, ASCENDING, DESCENDING
}

class UserListViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _userListState = MutableStateFlow<Resource<List<User>>>(Resource.Loading())
    val userListState = _userListState.asStateFlow()

    private var originalUserList: List<User> = emptyList()

    init {
        fetchUsers()
    }

    fun fetchUsers(limit: Int = 30, skip: Int = 0) {
        viewModelScope.launch {
            _userListState.value = Resource.Loading()
            when (val result = repository.getUsers(limit, skip)) {
                is Resource.Success -> {
                    originalUserList = result.data ?: emptyList()
                    _userListState.value = Resource.Success(originalUserList)
                }
                is Resource.Error -> {
                    _userListState.value = result
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    // FUNGSI BARU UNTUK MENGURUTKAN
    fun sortUsers(order: SortOrder) {
        val currentList = originalUserList
        val sortedList = when (order) {
            SortOrder.ASCENDING -> currentList.sortedBy { it.fullName }
            SortOrder.DESCENDING -> currentList.sortedByDescending { it.fullName }
            SortOrder.NONE -> currentList
        }
        _userListState.value = Resource.Success(sortedList)
    }
}