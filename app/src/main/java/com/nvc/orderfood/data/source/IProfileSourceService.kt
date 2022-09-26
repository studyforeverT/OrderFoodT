package com.nvc.orderfood.data.source

import android.net.Uri
import com.nvc.orderfood.model.User
import com.nvc.orderfood.utils.UiState
import kotlinx.coroutines.flow.Flow

interface IProfileSourceService {
    fun getProfile(): Flow<Result<User>>
    fun logout(result: (UiState<String>) -> Unit)
    fun updateProfileImage(uri: Uri, user: User): Flow<Result<Boolean>>
    fun updateInfo(user: User): Flow<Result<Boolean>>
}