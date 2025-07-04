package com.mohamed.myapplication.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohamed.domain.usecase.GetCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitySearchViewModel @Inject constructor(
    private val useCase: GetCitiesUseCase
) : ViewModel() {

    private var searchJob: Job? = null
    private val _uiState = MutableStateFlow(CitiesContract.CitySearchUiState(isLoading = true))
    val uiState: StateFlow<CitiesContract.CitySearchUiState> = _uiState.asStateFlow()

    private val _sideEffects = MutableSharedFlow<CitiesContract.CitySearchSideEffect>()
    val sideEffects = _sideEffects.asSharedFlow()


    init {
        onEvent(CitiesContract.CitySearchEvent.Init)
    }

    fun onEvent(event: CitiesContract.CitySearchEvent) {
        when (event) {
            is CitiesContract.CitySearchEvent.Init -> {
                loadCities("")
            }

            is CitiesContract.CitySearchEvent.OnSearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                observeSearchQuery(event.query)
            }
        }
    }

    private fun observeSearchQuery(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(200)
            loadCities(query)
        }
    }


    private fun loadCities(query: String = "") {
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = useCase.invoke(query)
                _uiState.update {
                    it.copy(
                        cities = result,
                        isEmpty = query.isNotEmpty() && result.isEmpty(),
                        isLoading = false,
                        listSize = result.size
                    )
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false) }
                _sideEffects.emit(CitiesContract.CitySearchSideEffect.ShowError("Error"))
            }
        }
    }
}