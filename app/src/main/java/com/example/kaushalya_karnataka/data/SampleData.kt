package com.example.kaushalya_karnataka.data

import com.example.kaushalya_karnataka.models.*

/**
 * High-quality sample data to ensure the app looks professional
 * even when offline or during initial evaluation.
 */
object SampleData {
    val workers = listOf(
        Worker(
            id = "1",
            name = "Manjunath Swamy",
            bio = "Highly skilled electrician with A-grade certification. Specializing in smart home wiring and industrial panels. 15 years of service in South Bangalore.",
            category = Category.ELECTRICIAN.name,
            location = "Jayanagar, Bangalore",
            rating = 4.9,
            reviewCount = 124,
            imageUrl = "https://images.unsplash.com/photo-1544717305-27a734ef4194?q=80&w=400&h=400&fit=crop",
            isVerified = true,
            skills = listOf("Smart Home", "Solar Wiring", "Safety Audit", "24/7 Service"),
            services = listOf(
                Service("s1", "UPS/Inverter Setup", 1500, PriceType.FIXED.name),
                Service("s2", "Full House Inspection", 800, PriceType.FIXED.name),
                Service("s3", "Panel Repair", 0, PriceType.STARTING_AT.name),
            ),
            updatedAt = System.currentTimeMillis() - 3600000
        ),
        Worker(
            id = "2",
            name = "Gowtham Patil",
            bio = "Specialist in modern CPVC and UPVC plumbing. Expert in pressure pump installations and high-end bathroom fittings. Certified by Leading Brands.",
            category = Category.PLUMBER.name,
            location = "Hubli Center",
            rating = 4.7,
            reviewCount = 89,
            imageUrl = "https://images.unsplash.com/photo-1517841905240-472988babdf9?q=80&w=400&h=400&fit=crop",
            isVerified = true,
            skills = "Leak Detection, Water Filters, Bathroom Renovation".split(", "),
            services = listOf(
                Service("s4", "Emergency Leak Fix", 500, PriceType.STARTING_AT.name),
                Service("s5", "Fixture Install", 300, PriceType.FIXED.name)
            ),
            updatedAt = System.currentTimeMillis() - 7200000
        ),
        Worker(
            id = "3",
            name = "Siddalingaiah V.",
            bio = "Master Carpenter for teak wood work and modular kitchens. We focus on durability and traditional designs. Serving Mysore and Mandya districts.",
            category = Category.CARPENTER.name,
            location = "Vijaynagar, Mysore",
            rating = 4.8,
            reviewCount = 56,
            imageUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?q=80&w=400&h=400&fit=crop",
            isVerified = true,
            skills = listOf("Teak Expert", "Modular Kitchen", "Furniture Restoration"),
            services = listOf(
                Service("s6", "Wardrobe Design", 0, PriceType.STARTING_AT.name),
                Service("s7", "Door Polish", 2500, PriceType.STARTING_AT.name)
            )
        ),
        Worker(
            id = "4",
            name = "Basavaraj M.",
            bio = "Precision welding for gates, grills, and structural sheds. Specialized in stainless steel and decorative iron work. Top quality finish.",
            category = Category.WELDER.name,
            location = "Mangalore Port Area",
            rating = 4.5,
            reviewCount = 42,
            imageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?q=80&w=400&h=400&fit=crop",
            skills = listOf("SS Welding", "Rolling Shutters", "Custom Gates"),
            updatedAt = System.currentTimeMillis() - 1500000
        )
    )

    fun getWorkerById(id: String): Worker? = workers.find { it.id == id }
}
