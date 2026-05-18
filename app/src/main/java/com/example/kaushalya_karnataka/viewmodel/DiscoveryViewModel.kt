package com.example.kaushalya_karnataka.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kaushalya_karnataka.data.SampleData
import com.example.kaushalya_karnataka.data.WorkerRepository
import com.example.kaushalya_karnataka.models.Category
import com.example.kaushalya_karnataka.models.Worker
import com.example.kaushalya_karnataka.util.Resource
import com.example.kaushalya_karnataka.util.WorkerSearch
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class DiscoveryUiState(
    val workers: List<Worker> = emptyList(),
    val filteredWorkers: List<Worker> = emptyList(),
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedCategory: Category? = null,
    val selectedLocation: String = "All Karnataka",
    val minRating: Double = 0.0,
    val availableLocations: List<String> = listOf("All Karnataka", "Bangalore", "Mysore", "Hubli", "Mangalore", "Belgaum")
)

class DiscoveryViewModel(private val repository: WorkerRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(DiscoveryUiState())
    val uiState: StateFlow<DiscoveryUiState> = _uiState.asStateFlow()

    init {
        loadWorkers()
    }

    fun loadWorkers() {
        viewModelScope.launch {
            repository.getWorkers().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.update { it.copy(isLoading = true) }
                    }
                    is Resource.Success -> {
                        val finalWorkers = if (resource.data.isNullOrEmpty()) SampleData.workers else resource.data
                        _uiState.update { it.copy(
                            workers = finalWorkers,
                            isLoading = false,
                            isRefreshing = false,
                            error = null
                        ) }
                        applyFilters()
                    }
                    is Resource.Error -> {
                        _uiState.update { it.copy(
                            workers = SampleData.workers, // Fallback for stability
                            isLoading = false,
                            isRefreshing = false,
                            error = resource.message
                        ) }
                        applyFilters()
                    }
                }
            }
        }
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true) }
        loadWorkers()
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onCategorySelected(category: Category?) {
        _uiState.update { it.copy(selectedCategory = category) }
        applyFilters()
    }

    fun onLocationSelected(location: String) {
        _uiState.update { it.copy(selectedLocation = location) }
        applyFilters()
    }

    fun onMinRatingChanged(rating: Double) {
        _uiState.update { it.copy(minRating = rating) }
        applyFilters()
    }

    private fun applyFilters() {
        _uiState.update { state ->
            val result = WorkerSearch.filterWorkers(
                workers = state.workers,
                query = state.searchQuery,
                category = state.selectedCategory?.name,
                location = state.selectedLocation,
                minRating = state.minRating
            )
            state.copy(filteredWorkers = result)
        }
    }
}

class DiscoveryViewModelFactory(private val repository: WorkerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiscoveryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiscoveryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
