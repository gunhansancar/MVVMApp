package com.gunhansancar.mvvmapp.ui.feature.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gunhansancar.mvvmapp.network.catfacts.CatFact
import com.gunhansancar.mvvmapp.network.catfacts.CatFactsRepository
import com.gunhansancar.mvvmapp.network.model.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MainUiState {
    object Init : MainUiState()
    object Loading : MainUiState()
    data class Success(val facts: List<CatFact>) : MainUiState()
    data class Error(val message: String) : MainUiState()
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val catFactsRepository: CatFactsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Init)
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        loadFacts()
    }

    private fun loadFacts() {
        _uiState.value = MainUiState.Loading
        viewModelScope.launch {
            val response = catFactsRepository.facts()
            if (response is Success) {
                _uiState.value = MainUiState.Success(response.value)
            } else {
                _uiState.value = MainUiState.Error("Couldn't fetch")
            }
        }
    }
}