package com.example.kaushalya_karnataka.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kaushalya_karnataka.data.WorkerRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MainViewModel(private val repository: WorkerRepository) : ViewModel() {
    private val _unreadLeadsCount = MutableStateFlow(0)
    val unreadLeadsCount: StateFlow<Int> = _unreadLeadsCount.asStateFlow()

    init {
        observeLeads()
    }

    private fun observeLeads() {
        val currentUser = FirebaseAuth.getInstance().currentUser ?: return
        viewModelScope.launch {
            repository.getWorkerStream(currentUser.uid)
                .catch { e ->
                    Log.e("MainViewModel", "Error observing leads", e)
                }
                .collect { worker ->
                    _unreadLeadsCount.value = (worker?.hireRequests ?: emptyList()).count { it.isContacted != true }
                }
        }
    }
}

class MainViewModelFactory(private val repository: WorkerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
