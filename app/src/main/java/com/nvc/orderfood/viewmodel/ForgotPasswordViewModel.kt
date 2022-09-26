package com.nvc.orderfood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nvc.orderfood.repository.AuthRepository
import com.nvc.orderfood.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(private val forgotPassword: AuthRepository) :
    ViewModel() {
    private val _forgotPasswordUser = MutableLiveData<UiState<String>>()
    val forgotPasswordUser: LiveData<UiState<String>>
        get() = _forgotPasswordUser

    fun sendEmailToResetPasswordUser(email: String) {
        _forgotPasswordUser.value = UiState.Loading
        forgotPassword.forgotPassword(email) {
            _forgotPasswordUser.value = it
        }
    }

}