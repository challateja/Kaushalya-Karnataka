package com.example.kaushalya_karnataka.data

import android.util.Log
import com.example.kaushalya_karnataka.models.Worker
import com.example.kaushalya_karnataka.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Repository class that abstracts the Firestore data source.
 * Implements the "Repository Pattern" for clean architecture.
 */
class WorkerRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val workersCollection = firestore.collection("workers")

    /**
     * Streams the list of workers from Firestore.
     * Returns a [Flow] of [Resource] to handle loading and error states.
     */
    fun getWorkers(): Flow<Resource<List<Worker>>> = callbackFlow {
        trySend(Resource.Loading())
        
        val subscription = workersCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("WorkerRepository", "Error fetching workers: ${error.message}")
                trySend(Resource.Error(error.localizedMessage ?: "Unknown Error"))
                return@addSnapshotListener
            }
            
            if (snapshot != null) {
                val workers = snapshot.documents.mapNotNull { doc ->
                    try {
                        doc.toObject(Worker::class.java)
                    } catch (e: Exception) {
                        Log.e("WorkerRepository", "Error parsing worker document ${doc.id}", e)
                        null
                    }
                }
                trySend(Resource.Success(workers))
            }
        }
        awaitClose { subscription.remove() }
    }

    /**
     * Streams a single worker's data by ID.
     */
    fun getWorkerStream(id: String): Flow<Resource<Worker?>> = callbackFlow {
        if (id.isBlank()) {
            trySend(Resource.Error("Invalid Worker ID"))
            close()
            return@callbackFlow
        }
        
        trySend(Resource.Loading())
        
        val subscription = workersCollection.document(id).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("WorkerRepository", "Error streaming worker $id: ${error.message}")
                trySend(Resource.Error(error.localizedMessage ?: "Unknown Error"))
                return@addSnapshotListener
            }
            
            try {
                val worker = snapshot?.toObject(Worker::class.java)
                trySend(Resource.Success(worker))
            } catch (e: Exception) {
                Log.e("WorkerRepository", "Error parsing worker $id", e)
                trySend(Resource.Error("Failed to parse worker data"))
            }
        }
        awaitClose { subscription.remove() }
    }

    suspend fun saveWorker(worker: Worker): Resource<Unit> {
        if (worker.id.isBlank()) return Resource.Error("Worker ID cannot be empty")
        return try {
            workersCollection.document(worker.id).set(worker).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Log.e("WorkerRepository", "Error saving worker", e)
            Resource.Error(e.localizedMessage ?: "Failed to save worker")
        }
    }
}
