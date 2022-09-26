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
class SignInViewModel @Inject constructor(private val signInRepository: AuthRepository) :
    ViewModel() {

    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login


    fun login(email: String, password: String) {
        _login.value = UiState.Loading
        signInRepository.loginUser(email, password) {
           viewModelScope.launch (Dispatchers.Main){
               _login.value = it
           }
        }
    }

    fun logout(){
        _login.value = UiState.Loading
        signInRepository.logout { }
    }

    fun resetState(){
        _login.value = UiState.Loading
    }

}