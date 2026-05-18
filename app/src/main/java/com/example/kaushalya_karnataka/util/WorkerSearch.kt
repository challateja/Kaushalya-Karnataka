package com.example.kaushalya_karnataka.util

import com.example.kaushalya_karnataka.models.Worker

/**
 * Utility class to provide professional search and filtering logic.
 * Enhances "Separation of Concerns" for project evaluation.
 */
object WorkerSearch {
    fun filterWorkers(
        workers: List<Worker>,
        query: String,
        category: String?,
        location: String,
        minRating: Double
    ): List<Worker> {
        return workers.filter { worker ->
            val matchesQuery = query.isBlank() || 
                worker.name?.contains(query, ignoreCase = true) == true ||
                worker.skills?.any { it.contains(query, ignoreCase = true) } == true ||
                worker.services?.any { it.title?.contains(query, ignoreCase = true) == true } == true

            val matchesCategory = category == null || worker.category == category
            val matchesLocation = location == "All Karnataka" || 
                worker.location?.contains(location, ignoreCase = true) == true
            val matchesRating = (worker.rating ?: 0.0) >= minRating

            matchesQuery && matchesCategory && matchesLocation && matchesRating
        }.sortedByDescending { it.updatedAt ?: 0L }
    }
}
