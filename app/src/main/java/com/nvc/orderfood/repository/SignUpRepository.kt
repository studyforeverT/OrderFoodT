package com.nvc.orderfood.repository

import com.nvc.orderfood.data.source.IAuthSoucreService
import com.nvc.orderfood.data.source.firebase.SignUpRemote
import com.nvc.orderfood.data.source.local.AuthLocal
import com.nvc.orderfood.model.User
import com.nvc.orderfood.utils.CheckNetwork
import com.nvc.orderfood.utils.UiState

class SignUpRepository(
    private val authRemote: SignUpRemote,
    private val checkNetwork: CheckNetwork,
    private val authLocal: AuthLocal
) : IAuthSoucreService {

    override fun registerUser(
        email: String,
        password: String,
        user: User,
        result: (UiState<String>) -> Unit
    ) {
        if (checkNetwork.isNetworkAvailable()) {
            authRemote.registerUser(email, password, user, result).apply {
                authLocal.registerInsertUser(user)
            }

        }else {
            checkNetwork.showToastNetworkError()
        }
    }

    override fun updateUserInfo(user: User, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }


    override fun logout(result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }

}