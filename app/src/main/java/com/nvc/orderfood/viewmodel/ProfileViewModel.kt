package com.nvc.orderfood.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nvc.orderfood.model.User
import com.nvc.orderfood.repository.ProfileRepository
import com.nvc.orderfood.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileRepository: ProfileRepository) :
    ViewModel() {

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun getDataProfile() {
        viewModelScope.launch {
            profileRepository.getProfile().collect{
                when{
                    it.isSuccess -> {
                        _user.value = it.getOrNull()
                    }
                    it.isFailure ->{

                    }
                }
            }
        }
    }

    private val _dataUser = MutableLiveData<UiState<String>>()

    private val _statusUpdateUser = MutableLiveData<Boolean>()
    val statusUpdateUser: LiveData<Boolean>
        get() = _statusUpdateUser

    fun updateDataUserImage(uri: Uri, user: User) {
        _dataUser.value = UiState.Loading
        _statusUpdateUser.value = false
        viewModelScope.launch {
            profileRepository.updateProfileImage(uri, user).collect{
                when {
                    it.isSuccess -> {
                        _statusUpdateUser.value = true
                    }
                    it.isFailure -> {
                        _statusUpdateUser.value = false
                    }
                }
            }
        }
    }

    fun updateDataUser(user: User) {
        _dataUser.value = UiState.Loading
        _statusUpdateUser.value = false
        viewModelScope.launch {
            profileRepository.updateInfo(user).collect{
                when {
                    it.isSuccess -> {
                        _statusUpdateUser.value = true
                    }
                    it.isFailure -> {
                        _statusUpdateUser.value = false
                    }
                }
            }
        }
    }
    fun updateStatus(){
        _statusUpdateUser.value = false
    }

}
