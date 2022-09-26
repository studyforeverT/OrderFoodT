package com.nvc.orderfood.data.source

import com.nvc.orderfood.model.User
import com.nvc.orderfood.utils.UiState


interface IAuthSoucreService {
    fun registerUser(email: String, password: String, user: User, result: (UiState<String>) -> Unit)
    fun updateUserInfo(user: User, result: (UiState<String>) -> Unit)
    fun logout(result: (UiState<String>) -> Unit)
}