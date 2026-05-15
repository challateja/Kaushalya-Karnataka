package com.example.kaushalya_karnataka.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kaushalya_karnataka.data.WorkerRepository
import com.example.kaushalya_karnataka.models.Category
import com.example.kaushalya_karnataka.models.Worker
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
    val maxPrice: Int = 10000,
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
            repository.getWorkers()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e -> 
                    _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                }
                .collect { workers ->
                    _uiState.update { it.copy(workers = workers, isLoading = false, isRefreshing = false, error = null) }
                    applyFilters()
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

    fun onMaxPriceChanged(price: Int) {
        _uiState.update { it.copy(maxPrice = price) }
        applyFilters()
    }

    private fun applyFilters() {
        _uiState.update { state ->
            var result = state.workers
            
            // Filter by Category
            state.selectedCategory?.let { category ->
                result = result.filter { it.category == category.name }
            }
            
            // Filter by Location
            if (state.selectedLocation != "All Karnataka") {
                result = result.filter { worker ->
                    worker.location?.contains(state.selectedLocation, ignoreCase = true) == true
                }
            }

            // Filter by Rating
            result = result.filter { (it.rating ?: 0.0) >= state.minRating }

            // Filter by Price (check if any service is below or equal to maxPrice)
            if (state.maxPrice < 10000) {
                result = result.filter { worker ->
                    val minServicePrice = worker.services?.minOfOrNull { it.price ?: 0 } ?: 0
                    minServicePrice <= state.maxPrice
                }
            }
            
            // Filter by Search Query (including Skills and Services)
            if (state.searchQuery.isNotBlank()) {
                result = result.filter { worker ->
                    val catDisplayName = worker.getCategoryEnum().displayName
                    val matchesSkills = worker.skills?.any { it.contains(state.searchQuery, ignoreCase = true) } == true
                    val matchesServices = worker.services?.any { it.title?.contains(state.searchQuery, ignoreCase = true) == true } == true
                    
                    (worker.name?.contains(state.searchQuery, ignoreCase = true) == true) ||
                    catDisplayName.contains(state.searchQuery, ignoreCase = true) ||
                    (worker.location?.contains(state.searchQuery, ignoreCase = true) == true) ||
                    matchesSkills ||
                    matchesServices
                }
            }
            
            // Sort by: Most Recent Updates first, then by Rating
            result = result.sortedWith(compareByDescending<Worker> { it.updatedAt ?: 0L }.thenByDescending { it.rating ?: 0.0 })

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
