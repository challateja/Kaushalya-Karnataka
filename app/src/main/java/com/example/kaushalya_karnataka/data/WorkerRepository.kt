package com.example.kaushalya_karnataka.data

import android.util.Log
import com.example.kaushalya_karnataka.models.Worker
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class WorkerRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val workersCollection = firestore.collection("workers")

    fun getWorkers(): Flow<List<Worker>> = callbackFlow {
        val subscription = workersCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("WorkerRepository", "Error fetching workers: ${error.message}")
                // Instead of close(error) which crashes, we send empty list on error
                trySend(emptyList())
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
                trySend(workers)
            }
        }
        awaitClose { subscription.remove() }
    }

    fun getWorkerStream(id: String): Flow<Worker?> = callbackFlow {
        if (id.isBlank()) {
            trySend(null)
            close()
            return@callbackFlow
        }
        val subscription = workersCollection.document(id).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.e("WorkerRepository", "Error streaming worker $id: ${error.message}")
                trySend(null)
                return@addSnapshotListener
            }
            try {
                trySend(snapshot?.toObject(Worker::class.java))
            } catch (e: Exception) {
                Log.e("WorkerRepository", "Error parsing worker $id", e)
                trySend(null)
            }
        }
        awaitClose { subscription.remove() }
    }

    suspend fun getWorkerById(id: String): Worker? {
        if (id.isBlank()) return null
        return try {
            workersCollection.document(id).get().await().toObject(Worker::class.java)
        } catch (e: Exception) {
            Log.e("WorkerRepository", "Error fetching worker by id: $id", e)
            null
        }
    }

    suspend fun saveWorker(worker: Worker) {
        if (worker.id.isBlank()) return
        try {
            workersCollection.document(worker.id).set(worker).await()
        } catch (e: Exception) {
            Log.e("WorkerRepository", "Error saving worker", e)
            throw e
        }
    }
}
