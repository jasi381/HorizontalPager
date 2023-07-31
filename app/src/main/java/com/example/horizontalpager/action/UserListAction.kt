package com.example.horizontalpager.action

import com.example.horizontalpager.data.User

sealed class UserListAction {
    object FetchUsers : UserListAction()
}

data class UserListState(
    val loading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String = ""
)

object UserListReducer {
    fun reduce(currentState: UserListState, action: UserListAction): UserListState {
        return when (action) {
            is UserListAction.FetchUsers -> currentState.copy(loading = true)
        }
    }
}
