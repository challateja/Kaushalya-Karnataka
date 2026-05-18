package com.example.kaushalya_karnataka

import com.example.kaushalya_karnataka.models.Worker
import com.example.kaushalya_karnataka.util.WorkerSearch
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WorkerSearchTest {

    private val sampleWorkers = listOf(
        Worker(id = "1", name = "John Doe", category = "Plumber", location = "Bangalore", rating = 4.5),
        Worker(id = "2", name = "Jane Smith", category = "Electrician", location = "Mysore", rating = 3.8),
        Worker(id = "3", name = "Bob Builder", category = "Plumber", location = "Bangalore", rating = 4.0)
    )

    @Test
    fun `filterWorkers by query matches name`() {
        val result = WorkerSearch.filterWorkers(sampleWorkers, "John", null, "All Karnataka", 0.0)
        assertEquals(1, result.size)
        assertEquals("John Doe", result[0].name)
    }

    @Test
    fun `filterWorkers by category`() {
        val result = WorkerSearch.filterWorkers(sampleWorkers, "", "Plumber", "All Karnataka", 0.0)
        assertEquals(2, result.size)
        assertTrue(result.all { it.category == "Plumber" })
    }

    @Test
    fun `filterWorkers by location`() {
        val result = WorkerSearch.filterWorkers(sampleWorkers, "", null, "Mysore", 0.0)
        assertEquals(1, result.size)
        assertEquals("Mysore", result[0].location)
    }

    @Test
    fun `filterWorkers by rating`() {
        val result = WorkerSearch.filterWorkers(sampleWorkers, "", null, "All Karnataka", 4.0)
        assertEquals(2, result.size)
        assertTrue(result.all { (it.rating ?: 0.0) >= 4.0 })
    }

    @Test
    fun `filterWorkers with no matches returns empty list`() {
        val result = WorkerSearch.filterWorkers(sampleWorkers, "NonExistent", null, "All Karnataka", 0.0)
        assertTrue(result.isEmpty())
    }
}
