package com.dorox.pinapp.presentation.screen.select_city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dorox.pinapp.domain.usecase.GetCitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectCityViewModel @Inject constructor(
    private val getCitiesUseCase: GetCitiesUseCase
): ViewModel() {

    private val _state = MutableStateFlow(CityState())
    val state = _state.asStateFlow()

    init {
        loadCities()
    }

    private fun loadCities() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            val cities = getCitiesUseCase()
            _state.update {
                it.copy(allCities = cities, isLoading = false)
            }
        }
    }

    fun onQueryChange(query: String) {
        _state.update {
            it.copy(query = query)
        }
    }

    fun clearQuery() {
        _state.update {
            it.copy(query = "")
        }
    }
}

data class CityState(
    val query: String = "",
    val allCities: List<String> = emptyList(),
    val isLoading: Boolean = false
) {
    val filteredCities: List<String>
        get() = if (query.isBlank())
            allCities
        else
            allCities.filter { cityName ->
                cityName.contains(query, ignoreCase = true)
            }
}