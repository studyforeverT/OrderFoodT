package com.nvc.orderfood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nvc.orderfood.model.User
import com.nvc.orderfood.repository.SignUpRepository
import com.nvc.orderfood.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpRepository: SignUpRepository) :
    ViewModel() {

    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register

    fun register(email: String, password: String, user: User){
        _register.value = UiState.Loading
        signUpRepository.registerUser(email, password, user) {
            _register.value = it
        }
    }

    fun reRegister(){
        _register.value = UiState.Loading
    }

}