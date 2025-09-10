package com.rama.casestudy.ui.features.user_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rama.casestudy.domain.model.User
import com.rama.casestudy.domain.repository.UserRepository
import com.rama.casestudy.util.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class SortOrder {
    NONE, ASCENDING, DESCENDING
}

class UserListViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.NONE)

    private val _usersResource = MutableStateFlow<Resource<List<User>>>(Resource.Loading())

    val userListState: StateFlow<Resource<List<User>>> =
        combine(_usersResource, _searchQuery, _sortOrder) { resource, query, order ->
            when (resource) {
                is Resource.Success -> {
                    val originalList = resource.data ?: emptyList()
                    val filteredList = if (query.isBlank()) {
                        originalList
                    } else {
                        originalList.filter {
                            it.fullName.contains(query, ignoreCase = true)
                        }
                    }
                    val sortedList = when (order) {
                        SortOrder.ASCENDING -> filteredList.sortedBy { it.fullName }
                        SortOrder.DESCENDING -> filteredList.sortedByDescending { it.fullName }
                        SortOrder.NONE -> filteredList
                    }
                    Resource.Success(sortedList)
                }
                is Resource.Error -> resource
                is Resource.Loading -> Resource.Loading()
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Resource.Loading())


    init {
        fetchUsers()
    }

    fun fetchUsers(limit: Int = 30, skip: Int = 0) {
        viewModelScope.launch {
            _usersResource.value = Resource.Loading()
            _usersResource.value = repository.getUsers(limit, skip)
        }
    }

    fun sortUsers(order: SortOrder) {
        _sortOrder.value = order
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }
}