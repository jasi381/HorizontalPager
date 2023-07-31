package com.example.horizontalpager.viewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.horizontalpager.action.UserListAction
import com.example.horizontalpager.action.UserListReducer
import com.example.horizontalpager.action.UserListState
import com.example.horizontalpager.api.RetrofitClient.apiService
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class UserListViewModel : ViewModel() {

    private val _state = MutableStateFlow<UserListState>(UserListState())
    val state: StateFlow<UserListState> = _state.asStateFlow()

    private val _action = Channel<UserListAction>()
    val action = _action.receiveAsFlow()

    init {
        viewModelScope.launch {
            handleActions()
        }
    }

    private suspend fun handleActions() {
        action.collect { action ->
            val currentState = _state.value
            val newState = UserListReducer.reduce(currentState, action)
            _state.value = newState

            when (action) {
                is UserListAction.FetchUsers -> fetchUsers()
            }
        }
    }

    private suspend fun fetchUsers() {
        try {
            val users = apiService.getUsers() // Replace apiService with your actual API service
            _state.value = _state.value.copy(users = users, loading = false)
        } catch (e: Exception) {
            _state.value = _state.value.copy(error = "Failed to fetch users", loading = false)
        }
    }

    fun fetchUsersList() {
        viewModelScope.launch {
            _action.send(UserListAction.FetchUsers)
        }
    }
}
