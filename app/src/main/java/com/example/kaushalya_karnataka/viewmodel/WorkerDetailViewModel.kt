package com.example.kaushalya_karnataka.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kaushalya_karnataka.data.WorkerRepository
import com.example.kaushalya_karnataka.models.HireRequest
import com.example.kaushalya_karnataka.models.Review
import com.example.kaushalya_karnataka.models.Worker
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WorkerDetailUiState(
    val worker: Worker? = null,
    val isLoading: Boolean = true,
    val isHiring: Boolean = false,
    val error: String? = null
)

class WorkerDetailViewModel(
    private val repository: WorkerRepository,
    private val workerId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkerDetailUiState())
    val uiState: StateFlow<WorkerDetailUiState> = _uiState.asStateFlow()

    init {
        loadWorker()
    }

    private fun loadWorker() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val worker = repository.getWorkerById(workerId)
            if (worker != null) {
                _uiState.update { it.copy(worker = worker, isLoading = false) }
            } else {
                _uiState.update { it.copy(isLoading = false, error = "Worker not found") }
            }
        }
    }

    fun hireWorker(message: String, onSuccess: () -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val currentWorker = _uiState.value.worker ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isHiring = true) }
            
            val newRequest = HireRequest(
                id = System.currentTimeMillis().toString(),
                customerName = currentUser.displayName ?: "Anonymous",
                phone = currentUser.phoneNumber ?: "", 
                message = message,
                isContacted = false
            )

            val updatedWorker = currentWorker.copy(
                hireRequests = (currentWorker.hireRequests ?: emptyList()) + newRequest
            )

            try {
                repository.saveWorker(updatedWorker)
                _uiState.update { it.copy(isHiring = false, worker = updatedWorker) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(isHiring = false, error = "Failed to send request") }
            }
        }
    }

    fun addReview(rating: Int, text: String, onSuccess: () -> Unit) {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        val currentWorker = _uiState.value.worker ?: return

        viewModelScope.launch {
            val newReview = Review(
                id = System.currentTimeMillis().toString(),
                authorName = currentUser.displayName ?: "Verified Neighbor",
                text = text,
                rating = rating,
                timestamp = "Just now"
            )
            
            val currentReviews = currentWorker.reviews ?: emptyList()
            val totalReviews = currentReviews.size + 1
            val newAvgRating = ((currentWorker.rating ?: 0.0) * currentReviews.size + rating) / totalReviews

            val updatedWorker = currentWorker.copy(
                reviews = currentReviews + newReview,
                reviewCount = totalReviews,
                rating = newAvgRating
            )

            try {
                repository.saveWorker(updatedWorker)
                _uiState.update { it.copy(worker = updatedWorker) }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to submit review") }
            }
        }
    }
}

class WorkerDetailViewModelFactory(
    private val repository: WorkerRepository,
    private val workerId: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkerDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WorkerDetailViewModel(repository, workerId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
