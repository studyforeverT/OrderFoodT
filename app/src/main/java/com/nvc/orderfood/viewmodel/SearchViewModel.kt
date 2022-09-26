package com.nvc.orderfood.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nvc.orderfood.model.Food
import com.nvc.orderfood.repository.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _result = MutableLiveData<ArrayList<Food>>()

    val result: LiveData<ArrayList<Food>>
        get() = _result

    private val _key = MutableStateFlow("")
    val key : StateFlow<String>
        get() = _key


    fun searchByName(name: String) {
        viewModelScope.launch {
            searchRepository.getByName(name).collect{
                _result.value = it as ArrayList<Food> /* = java.util.ArrayList<com.nvc.orderfood.model.Food> */
            }
        }
    }

    fun updateKey(s : String) {
        _key.value = s
    }
}