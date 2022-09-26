package com.nvc.orderfood.repository

import android.net.Uri
import com.nvc.orderfood.data.source.IProfileSourceService
import com.nvc.orderfood.data.source.firebase.ProfileRemote
import com.nvc.orderfood.model.User
import com.nvc.orderfood.utils.UiState
import kotlinx.coroutines.flow.Flow


class ProfileRepository(private val profileRemote: ProfileRemote): IProfileSourceService {
    private var isCacheDirty = true

    override fun getProfile(): Flow<Result<User>> {
        return profileRemote.getProfile()
    }

    override fun updateProfileImage(uri: Uri, user: User): Flow<Result<Boolean>> {
        return profileRemote.updateProfileImage(uri, user)
    }

    override fun updateInfo(user: User): Flow<Result<Boolean>> {
        return profileRemote.updateInfo(user)
    }

    override fun logout(result: (UiState<String>) -> Unit) {
        if (!isCacheDirty) {
            profileRemote.logout(result)
        }
    }


}