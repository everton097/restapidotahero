package com.example.restapi_dotahero.views

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.network.HttpException
import com.example.restapi_dotahero.data.Hero
import com.example.restapi_dotahero.network.OpenDotaApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

sealed interface HerosUiState {
    object Loading : HerosUiState

    data class Success(val heroes: List<Hero>) : HerosUiState

    object Error : HerosUiState
}

class HerosViewModel : ViewModel() {

    private var _uiState: MutableStateFlow<HerosUiState> = MutableStateFlow(HerosUiState.Loading)
    val uiState: StateFlow<HerosUiState> = _uiState.asStateFlow()

    init {
        getHeroes()
    }

    private fun getHeroes() {
        viewModelScope.launch {
            try {
                _uiState.value = HerosUiState.Success(OpenDotaApi.retrofitService.getHeros())
                Log.e("MarsViewModel", "Success: ${_uiState.value}")
            } catch (e: IOException) {
                _uiState.value = HerosUiState.Error
                Log.e("MarsViewModel", "Failure IOException: ${_uiState.value}")
                Log.e("MarsViewModel", "Failure IOException: ${e.message}")
            } catch (e: HttpException) {
                _uiState.value = HerosUiState.Error
                Log.e("MarsViewModel", "Failure HttpException: ${e.message}")
            } catch (e: Exception) {
                Log.e("MarsViewModel", "An unexpected error occurred: ${e.message}")
            }
        }
    }
}