package com.nvc.orderfood.viewmodel

import androidx.lifecycle.*
import com.nvc.orderfood.model.Favorite
import com.nvc.orderfood.repository.FavoriteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(private val favoriteRepository: FavoriteRepository) :
    ViewModel() {

    val favoriteList = favoriteRepository.getListFavoriteLiveData()
    fun getDataRemote() {
        favoriteRepository.getDataRemote()
    }

    val loveOfFavoriteItems = MutableLiveData(false)

    fun addToFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoriteRepository.apply {
                insertFavoriteRemote(favorite).apply {
                    if (checkNetwork()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            collect {
                                insertFavoriteLocal(favorite)
                            }
                        }
                        loveOfFavoriteItems.postValue(true)
                    } else {
                        showToastNW()
                    }
                }
            }
        }
    }

    fun removeItemFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoriteRepository.apply {
                deleteFavoriteRemote(favorite).apply {
                    if (checkNetwork()) {
                        CoroutineScope(Dispatchers.IO).launch {
                            collect {
                                deleteFavoriteLocal(favorite)
                            }
                        }
                        loveOfFavoriteItems.postValue(false)
                    } else {
                        showToastNW()
                    }
                }
            }
        }
    }
}