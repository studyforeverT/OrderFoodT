package com.nvc.orderfood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nvc.orderfood.repository.AuthRepository
import com.nvc.orderfood.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangePasswordViewModel @Inject constructor(private val changePassword: AuthRepository) :
    ViewModel() {
    private val _changePasswordUser = MutableLiveData<UiState<String>>()
    val changePasswordUser: LiveData<UiState<String>>
        get() = _changePasswordUser

    fun updatePassword(password: String) {
        _changePasswordUser.value = UiState.Loading
        changePassword.changePassword(password) {
            viewModelScope.launch(Dispatchers.Main) {
                _changePasswordUser.value = it
                _changePasswordUser.value = UiState.Loading
            }
        }

    }
}