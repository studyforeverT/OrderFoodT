package com.nvc.orderfood.repository

import android.util.Log
import com.google.gson.Gson
import com.nvc.orderfood.data.source.IAuthSoucreService
import com.nvc.orderfood.data.source.firebase.AuthRemote
import com.nvc.orderfood.model.User
import com.nvc.orderfood.utils.CheckNetwork
import com.nvc.orderfood.utils.MySharePre
import com.nvc.orderfood.utils.UiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AuthRepository(
    private val authRemote: AuthRemote,
    private val checkNetwork: CheckNetwork,
    private val mySharePre: MySharePre
) :
    IAuthSoucreService {

    override fun registerUser(
        email: String,
        password: String,
        user: User,
        result: (UiState<String>) -> Unit
    ) {
        TODO("Not yet implemented")
    }

    override fun updateUserInfo(user: User, result: (UiState<String>) -> Unit) {
        TODO("Not yet implemented")
    }

    fun loginUser(
        email: String,
        password: String,
        result: (UiState<String>) -> Unit
    ) {
        if (checkNetwork.isNetworkAvailable()) {
            Log.d("TAGGGGG", "loginUser: Login")
            CoroutineScope(Dispatchers.IO).launch {
                authRemote.loginUser(email, password, result).collect {
                    when {
                        it.isSuccess -> {
                            mySharePre.putString("user", Gson().toJson(it.getOrNull()))
                        }
                        it.isFailure -> {

                        }
                    }
                }
            }
        } else {
            checkNetwork.showToastNetworkError()
        }
    }

    fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        if (checkNetwork.isNetworkAvailable()) {
            CoroutineScope(Dispatchers.IO).launch {
                authRemote.forgotPassword(email, result).collect {
                    when {
                        it.isSuccess -> {
                            withContext(Dispatchers.Main) {
                                result.invoke(UiState.Success("Please check your email!"))
                            }
                        }
                    }
                }
            }
        } else {
            checkNetwork.showToastNetworkError()
        }
    }

    fun changePassword(password: String, result: (UiState<String>) -> Unit) {
        if (checkNetwork.isNetworkAvailable()) {
            CoroutineScope(Dispatchers.IO).launch {
                authRemote.changePassword(password, result).collect {}
            }
        } else {
            checkNetwork.showToastNetworkError()
        }
    }

    override fun logout(result: (UiState<String>) -> Unit) {
        if (checkNetwork.isNetworkAvailable()) {
            authRemote.logout(result)
        } else {
            checkNetwork.showToastNetworkError()
        }
    }

}