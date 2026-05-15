package com.example.kaushalya_karnataka.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kaushalya_karnataka.data.WorkerRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class SplashDestination {
    object Loading : SplashDestination()
    object Login : SplashDestination()
    object Discovery : SplashDestination()
    object ProfileSetup : SplashDestination()
}

class SplashViewModel(private val repository: WorkerRepository) : ViewModel() {
    private val _destination = MutableStateFlow<SplashDestination>(SplashDestination.Loading)
    val destination: StateFlow<SplashDestination> = _destination.asStateFlow()

    init {
        checkAuthState()
    }

    fun checkAuthState() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            _destination.value = SplashDestination.Login
        } else {
            viewModelScope.launch {
                val worker = repository.getWorkerById(currentUser.uid)
                if (worker == null || worker.name.isNullOrBlank() || worker.location.isNullOrBlank()) {
                    _destination.value = SplashDestination.ProfileSetup
                } else {
                    _destination.value = SplashDestination.Discovery
                }
            }
        }
    }
}

class SplashViewModelFactory(private val repository: WorkerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SplashViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SplashViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
