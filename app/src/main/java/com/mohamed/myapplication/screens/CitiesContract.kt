package com.mohamed.myapplication.screens

import com.mohamed.domain.entity.CityEntity

object CitiesContract {

    data class CitySearchUiState(
        val searchQuery: String = "",
        val cities: List<CityEntity> = emptyList(),
        val isLoading: Boolean = false,
        val isEmpty: Boolean = false,
        val listSize: Int = 0
    )

    sealed interface CitySearchEvent {
        data class OnSearchQueryChanged(val query: String) : CitySearchEvent
        object Init : CitySearchEvent
    }
    sealed interface CitySearchSideEffect {
        data class ShowError(val message: String) : CitySearchSideEffect
    }
}