package com.example.kaushalya_karnataka.models

import com.google.firebase.firestore.DocumentId

enum class Category(val displayName: String) {
    ELECTRICIAN("Electrician"),
    PLUMBER("Plumber"),
    CARPENTER("Carpenter"),
    PAINTER("Painter"),
    MASON("Mason"),
    AC_TECHNICIAN("AC Technician"),
    WELDER("Welder"),
    OTHER("Other")
}

enum class PriceType(val label: String) {
    FIXED("Fixed"),
    STARTING_AT("Starting at")
}

data class Worker(
    @DocumentId
    var id: String = "",
    var name: String? = null,
    var bio: String? = null,
    var category: String? = "OTHER",
    var location: String? = null,
    var rating: Double? = 0.0,
    var reviewCount: Int? = 0,
    var imageUrl: String? = null,
    var isVerified: Boolean? = false,
    var services: List<Service>? = emptyList(),
    var skills: List<String>? = emptyList(),
    var portfolioImages: List<PortfolioImage>? = emptyList(),
    var reviews: List<Review>? = emptyList(),
    var hireRequests: List<HireRequest>? = emptyList(),
    var updatedAt: Long? = 0L
) {
    fun getCategoryEnum(): Category = try { Category.valueOf(category ?: "OTHER") } catch (e: Exception) { Category.OTHER }
}

data class Service(
    var id: String = "",
    var title: String? = null,
    var price: Int? = 0,
    var priceType: String? = "FIXED"
) {
    fun getPriceTypeEnum(): PriceType = try { PriceType.valueOf(priceType ?: "FIXED") } catch (e: Exception) { PriceType.FIXED }
}

data class PortfolioImage(
    var id: String = "",
    var imageUrl: String? = null,
    var description: String? = null
)

data class Review(
    var id: String = "",
    var authorName: String? = null,
    var text: String? = null,
    var rating: Int? = 0,
    var timestamp: String? = null
)

data class HireRequest(
    var id: String = "",
    var customerName: String? = null,
    var phone: String? = null,
    var message: String? = null,
    var isContacted: Boolean? = false
)
