package com.example.kaushalya_karnataka.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.kaushalya_karnataka.data.WorkerRepository
import com.example.kaushalya_karnataka.models.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileEditorUiState(
    val worker: Worker? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val name: String = "",
    val bio: String = "",
    val selectedCategory: Category = Category.OTHER,
    val location: String = "",
    val imageUrl: String? = null,
    val services: List<Service> = emptyList(),
    val skills: List<String> = emptyList(),
    val hireRequests: List<HireRequest> = emptyList(),
    val portfolioImages: List<PortfolioImage> = emptyList(),
    val unreadLeadsCount: Int = 0,
    val error: String? = null
)

class ProfileEditorViewModel(private val repository: WorkerRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileEditorUiState())
    val uiState: StateFlow<ProfileEditorUiState> = _uiState.asStateFlow()

    init {
        observeProfile()
    }

    private fun observeProfile() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (currentUser == null) {
            _uiState.update { it.copy(isLoading = false) }
            return
        }

        viewModelScope.launch {
            repository.getWorkerStream(currentUser.uid)
                .catch { e ->
                    Log.e("ProfileEditorViewModel", "Error observing profile", e)
                    _uiState.update { it.copy(isLoading = false, error = e.localizedMessage) }
                }
                .collect { worker ->
                    val currentWorker = worker ?: Worker(id = currentUser.uid, name = currentUser.displayName ?: "")
                    
                    _uiState.update { state ->
                        state.copy(
                            worker = currentWorker,
                            isLoading = false,
                            name = if (state.name.isEmpty()) currentWorker.name ?: "" else state.name,
                            bio = if (state.bio.isEmpty()) currentWorker.bio ?: "" else state.bio,
                            selectedCategory = if (state.name.isEmpty()) currentWorker.getCategoryEnum() else state.selectedCategory,
                            location = if (state.location.isEmpty()) currentWorker.location ?: "" else state.location,
                            imageUrl = if (state.imageUrl == null) currentWorker.imageUrl else state.imageUrl,
                            services = currentWorker.services ?: emptyList(),
                            skills = currentWorker.skills ?: emptyList(),
                            hireRequests = currentWorker.hireRequests ?: emptyList(),
                            portfolioImages = currentWorker.portfolioImages ?: emptyList(),
                            unreadLeadsCount = (currentWorker.hireRequests ?: emptyList()).count { it.isContacted != true }
                        )
                    }
                }
        }
    }

    fun onNameChange(newName: String) { _uiState.update { it.copy(name = newName) } }
    fun onBioChange(newBio: String) { _uiState.update { it.copy(bio = newBio) } }
    fun onCategoryChange(newCategory: Category) { _uiState.update { it.copy(selectedCategory = newCategory) } }
    fun onLocationChange(newLocation: String) { _uiState.update { it.copy(location = newLocation) } }
    fun onImageChange(newImageUrl: String) { _uiState.update { it.copy(imageUrl = newImageUrl) } }

    fun addSkill(skill: String) {
        if (skill.isBlank()) return
        val currentSkills = _uiState.value.skills.toMutableList()
        if (!currentSkills.contains(skill)) {
            currentSkills.add(skill)
            _uiState.update { it.copy(skills = currentSkills) }
        }
    }

    fun removeSkill(skill: String) {
        val currentSkills = _uiState.value.skills.filter { it != skill }
        _uiState.update { it.copy(skills = currentSkills) }
    }

    fun addOrUpdateService(id: String?, title: String, price: Int, priceType: PriceType) {
        val currentServices = _uiState.value.services.toMutableList()
        if (id == null) {
            val newService = Service(
                id = System.currentTimeMillis().toString(),
                title = title,
                price = price,
                priceType = priceType.name
            )
            currentServices.add(newService)
        } else {
            val index = currentServices.indexOfFirst { it.id == id }
            if (index != -1) {
                currentServices[index] = currentServices[index].copy(
                    title = title, 
                    price = price,
                    priceType = priceType.name
                )
            }
        }
        _uiState.update { it.copy(services = currentServices) }
    }

    fun removeService(serviceId: String) {
        _uiState.update { it.copy(services = it.services.filter { s -> s.id != serviceId }) }
    }

    fun addPortfolioImage(imageUrl: String, description: String) {
        val newImage = PortfolioImage(
            id = System.currentTimeMillis().toString(),
            imageUrl = imageUrl,
            description = description
        )
        _uiState.update { it.copy(portfolioImages = it.portfolioImages + newImage) }
    }

    fun removePortfolioImage(imageId: String) {
        _uiState.update { it.copy(portfolioImages = it.portfolioImages.filter { it.id != imageId }) }
    }

    fun toggleRequestContacted(requestId: String) {
        val currentWorker = _uiState.value.worker ?: return
        val updatedRequests = (currentWorker.hireRequests ?: emptyList()).map { req ->
            if (req.id == requestId) {
                req.copy(isContacted = !(req.isContacted ?: false))
            } else req
        }
        
        viewModelScope.launch {
            try {
                repository.saveWorker(currentWorker.copy(hireRequests = updatedRequests))
            } catch (e: Exception) {
                Log.e("ProfileEditorViewModel", "Error toggling contacted", e)
            }
        }
    }

    fun deleteLead(requestId: String) {
        val currentWorker = _uiState.value.worker ?: return
        val updatedRequests = (currentWorker.hireRequests ?: emptyList()).filter { it.id != requestId }
        
        viewModelScope.launch {
            try {
                repository.saveWorker(currentWorker.copy(hireRequests = updatedRequests))
            } catch (e: Exception) {
                Log.e("ProfileEditorViewModel", "Error deleting lead", e)
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        FirebaseAuth.getInstance().signOut()
        onSuccess()
    }

    fun saveProfile(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        val workerToSave = currentState.worker?.copy(
            name = currentState.name,
            bio = currentState.bio,
            category = currentState.selectedCategory.name,
            location = currentState.location,
            imageUrl = currentState.imageUrl,
            services = currentState.services,
            skills = currentState.skills,
            portfolioImages = currentState.portfolioImages,
            updatedAt = System.currentTimeMillis() // Set update timestamp
        ) ?: return

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isSaving = true) }
                repository.saveWorker(workerToSave)
                _uiState.update { it.copy(isSaving = false) }
                onSuccess()
            } catch (e: Exception) {
                Log.e("ProfileEditorViewModel", "Error saving profile", e)
                _uiState.update { it.copy(isSaving = false, error = e.localizedMessage) }
            }
        }
    }
}

class ProfileEditorViewModelFactory(private val repository: WorkerRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileEditorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileEditorViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
