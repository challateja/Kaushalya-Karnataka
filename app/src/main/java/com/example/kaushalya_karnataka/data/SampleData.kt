package com.example.kaushalya_karnataka.data

import com.example.kaushalya_karnataka.models.*

object SampleData {
    val workers = listOf(
        Worker(
            id = "1",
            name = "Ramesh Kumar",
            bio = "Experienced electrician with 12 years of expertise in residential and commercial wiring. Certified in modern safety standards.",
            category = Category.ELECTRICIAN.name,
            location = "Jayanagar, Bangalore",
            rating = 4.8,
            reviewCount = 47,
            imageUrl = "https://picsum.photos/seed/worker1/400/400",
            isVerified = true,
            services = listOf(
                Service("s1", "Fan Installation", 250, PriceType.FIXED.name),
                Service("s2", "Switchboard Repair", 150, PriceType.FIXED.name),
                Service("s3", "Full House Wiring", 5000, PriceType.STARTING_AT.name),
                Service("s4", "MCB/Fuse Replacement", 200, PriceType.FIXED.name),
            ),
            portfolioImages = listOf(
                PortfolioImage("p1", "https://picsum.photos/seed/elec1/600/400", "Modern switchboard"),
                PortfolioImage("p2", "https://picsum.photos/seed/elec2/600/400", "Commercial wiring"),
                PortfolioImage("p3", "https://picsum.photos/seed/elec3/600/400", "LED panel setup"),
                PortfolioImage("p4", "https://picsum.photos/seed/elec4/600/400", "Safety inspection"),
            ),
            reviews = listOf(
                Review("r1", "Suresh M.", "Excellent work! Fixed all wiring issues. Very professional.", 5, "2 days ago"),
                Review("r2", "Priya S.", "Quick and reliable. Fair pricing. Will hire again.", 4, "1 week ago"),
                Review("r3", "Amit K.", "Good work on the switchboard. Came on time.", 5, "2 weeks ago"),
            ),
            hireRequests = listOf(
                HireRequest("h1", "Deepa R.", "9876543210", "Need full house rewiring for 2BHK", false),
                HireRequest("h2", "Karthik V.", "9876543211", "Fan not working, urgent fix needed", true),
            )
        ),
        Worker(
            id = "2",
            name = "Sunil Gowda",
            bio = "Master plumber specializing in modern bathroom fittings and water purifier installations. Quick response for emergency leaks.",
            category = Category.PLUMBER.name,
            location = "Koramangala, Bangalore",
            rating = 4.6,
            reviewCount = 33,
            imageUrl = "https://picsum.photos/seed/worker2/400/400",
            isVerified = true,
            services = listOf(
                Service("s5", "Tap Repair", 100, PriceType.FIXED.name),
                Service("s6", "Pipe Leakage Fix", 300, PriceType.STARTING_AT.name),
                Service("s7", "Bathroom Fitting", 2000, PriceType.STARTING_AT.name),
                Service("s8", "Water Purifier Install", 500, PriceType.FIXED.name),
            ),
            portfolioImages = listOf(
                PortfolioImage("p5", "https://picsum.photos/seed/plumb1/600/400", "Bathroom renovation"),
                PortfolioImage("p6", "https://picsum.photos/seed/plumb2/600/400", "Kitchen plumbing"),
                PortfolioImage("p7", "https://picsum.photos/seed/plumb3/600/400", "Water heater install"),
            ),
            reviews = listOf(
                Review("r4", "Rekha P.", "Fixed a major leak in 30 minutes. Lifesaver!", 5, "3 days ago"),
                Review("r5", "Mohan L.", "Good bathroom fittings. Reasonable price.", 4, "1 week ago"),
            )
        ),
        Worker(
            id = "3",
            name = "Vishwanath Shetty",
            bio = "Third-generation carpenter crafting custom furniture, modular kitchens, and wooden interiors. Premium finish guaranteed.",
            category = Category.CARPENTER.name,
            location = "Malleshwaram, Bangalore",
            rating = 4.9,
            reviewCount = 62,
            imageUrl = "https://picsum.photos/seed/worker3/400/400",
            isVerified = true,
            services = listOf(
                Service("s9", "Custom Wardrobe", 8000, PriceType.STARTING_AT.name),
                Service("s10", "Door Repair", 500, PriceType.FIXED.name),
                Service("s11", "Modular Kitchen", 25000, PriceType.STARTING_AT.name),
                Service("s12", "Furniture Polish", 1500, PriceType.STARTING_AT.name),
            ),
            portfolioImages = listOf(
                PortfolioImage("p8", "https://picsum.photos/seed/carp1/600/400", "Custom teak wardrobe"),
                PortfolioImage("p9", "https://picsum.photos/seed/carp2/600/400", "Modular kitchen"),
                PortfolioImage("p10", "https://picsum.photos/seed/carp3/600/400", "Wooden ceiling panels"),
                PortfolioImage("p11", "https://picsum.photos/seed/carp4/600/400", "Bookshelf unit"),
            ),
            reviews = listOf(
                Review("r6", "Anitha G.", "Stunning wardrobe! The craftsmanship is incredible.", 5, "1 day ago"),
                Review("r7", "Rajesh N.", "Built a beautiful bookshelf. Worth every rupee.", 5, "4 days ago"),
                Review("r8", "Meena K.", "Great modular kitchen. Very professional.", 5, "2 weeks ago"),
            )
        ),
        Worker(
            id = "4",
            name = "Manjunath B.",
            bio = "Professional painter with expertise in interior, exterior, and texture painting. 8 years transforming homes across Bangalore.",
            category = Category.PAINTER.name,
            location = "Whitefield, Bangalore",
            rating = 4.5,
            reviewCount = 28,
            imageUrl = "https://picsum.photos/seed/worker4/400/400",
            isVerified = false,
            services = listOf(
                Service("s13", "Single Room Paint", 2500, PriceType.STARTING_AT.name),
                Service("s14", "Full House Paint", 15000, PriceType.STARTING_AT.name),
                Service("s15", "Texture Wall", 3000, PriceType.STARTING_AT.name),
                Service("s16", "Waterproofing", 4000, PriceType.STARTING_AT.name),
            ),
            portfolioImages = listOf(
                PortfolioImage("p12", "https://picsum.photos/seed/paint1/600/400", "Living room makeover"),
                PortfolioImage("p13", "https://picsum.photos/seed/paint2/600/400", "Texture wall finish"),
                PortfolioImage("p14", "https://picsum.photos/seed/paint3/600/400", "Exterior painting"),
            ),
            reviews = listOf(
                Review("r9", "Sanjay T.", "Beautiful texture work. Looks premium.", 5, "5 days ago"),
                Review("r10", "Kavitha R.", "Good painting but took extra time.", 3, "2 weeks ago"),
            )
        ),
        Worker(
            id = "5",
            name = "Nagesh Reddy",
            bio = "Expert mason and civil contractor. Specializing in tile work, plastering, and minor construction.",
            category = Category.MASON.name,
            location = "HSR Layout, Bangalore",
            rating = 4.7,
            reviewCount = 41,
            imageUrl = "https://picsum.photos/seed/worker5/400/400",
            isVerified = true,
            services = listOf(
                Service("s17", "Floor Tiling (per sqft)", 35, PriceType.FIXED.name),
                Service("s18", "Wall Plastering (per sqft)", 25, PriceType.FIXED.name),
                Service("s19", "Bathroom Renovation", 12000, PriceType.STARTING_AT.name),
                Service("s20", "Brick Work", 500, PriceType.STARTING_AT.name),
            ),
            portfolioImages = listOf(
                PortfolioImage("p15", "https://picsum.photos/seed/mason1/600/400", "Italian marble floor"),
                PortfolioImage("p16", "https://picsum.photos/seed/mason2/600/400", "Bathroom tiles"),
                PortfolioImage("p17", "https://picsum.photos/seed/mason3/600/400", "Wall construction"),
                PortfolioImage("p18", "https://picsum.photos/seed/mason4/600/400", "Patio renovation"),
            ),
            reviews = listOf(
                Review("r11", "Venkat S.", "Perfect tile alignment. Very meticulous.", 5, "1 week ago"),
                Review("r12", "Geetha H.", "Excellent bathroom renovation.", 5, "3 weeks ago"),
            )
        ),
        Worker(
            id = "6",
            name = "Ravi Shankar",
            bio = "Certified AC technician for all brands. Installation, servicing, and gas refilling. Same-day service.",
            category = Category.AC_TECHNICIAN.name,
            location = "Indiranagar, Bangalore",
            rating = 4.4,
            reviewCount = 19,
            imageUrl = "https://picsum.photos/seed/worker6/400/400",
            isVerified = true,
            services = listOf(
                Service("s21", "AC Service", 600, PriceType.FIXED.name),
                Service("s22", "AC Installation", 1500, PriceType.FIXED.name),
                Service("s23", "Gas Refilling", 2000, PriceType.STARTING_AT.name),
                Service("s24", "AC Repair", 800, PriceType.STARTING_AT.name),
            ),
            portfolioImages = listOf(
                PortfolioImage("p19", "https://picsum.photos/seed/ac1/600/400", "Split AC installation"),
                PortfolioImage("p20", "https://picsum.photos/seed/ac2/600/400", "Central AC maintenance"),
            ),
            reviews = listOf(
                Review("r13", "Pooja M.", "Quick service. Came within an hour.", 4, "2 days ago"),
                Review("r14", "Arjun D.", "Good install but slightly expensive.", 3, "1 week ago"),
            )
        ),
        Worker(
            id = "7",
            name = "Basavaraj Patil",
            bio = "Expert welder with experience in iron gates, grills, and structural steel work. Quality welding with clean finish.",
            category = Category.WELDER.name,
            location = "Rajajinagar, Bangalore",
            rating = 4.3,
            reviewCount = 15,
            imageUrl = "https://picsum.photos/seed/worker7/400/400",
            isVerified = false,
            services = listOf(
                Service("s25", "Iron Gate", 5000, PriceType.STARTING_AT.name),
                Service("s26", "Window Grill", 2000, PriceType.STARTING_AT.name),
                Service("s27", "Staircase Railing", 3500, PriceType.STARTING_AT.name),
                Service("s28", "Repair Welding", 500, PriceType.STARTING_AT.name),
            ),
            portfolioImages = listOf(
                PortfolioImage("p21", "https://picsum.photos/seed/weld1/600/400", "Custom iron gate"),
                PortfolioImage("p22", "https://picsum.photos/seed/weld2/600/400", "Decorative window grill"),
                PortfolioImage("p23", "https://picsum.photos/seed/weld3/600/400", "Spiral staircase railing"),
            ),
            reviews = listOf(
                Review("r15", "Manoj K.", "Strong and beautiful gate. Great craftsmanship.", 5, "1 week ago"),
            )
        ),
        Worker(
            id = "8",
            name = "Santosh Hegde",
            bio = "Versatile electrician and smart home specialist. From basic wiring to home automation and CCTV installations.",
            category = Category.ELECTRICIAN.name,
            location = "BTM Layout, Bangalore",
            rating = 4.7,
            reviewCount = 35,
            imageUrl = "https://picsum.photos/seed/worker8/400/400",
            isVerified = true,
            services = listOf(
                Service("s29", "CCTV Installation", 3000, PriceType.STARTING_AT.name),
                Service("s30", "Smart Switch Setup", 800, PriceType.FIXED.name),
                Service("s31", "Inverter Installation", 1500, PriceType.FIXED.name),
                Service("s32", "General Wiring", 300, PriceType.STARTING_AT.name),
            ),
            portfolioImages = listOf(
                PortfolioImage("p24", "https://picsum.photos/seed/smart1/600/400", "4-camera CCTV setup"),
                PortfolioImage("p25", "https://picsum.photos/seed/smart2/600/400", "Smart home panel"),
                PortfolioImage("p26", "https://picsum.photos/seed/smart3/600/400", "LED strip installation"),
            ),
            reviews = listOf(
                Review("r16", "Naveen R.", "Installed CCTV in my house. Very professional.", 5, "3 days ago"),
                Review("r17", "Sunita P.", "Smart switches work great. Good explanation.", 4, "1 week ago"),
                Review("r18", "Harish G.", "Reliable electrician. Always on time.", 5, "2 weeks ago"),
            )
        ),
    )

    fun getWorkerById(id: String): Worker? = workers.find { it.id == id }

    fun searchWorkers(query: String): List<Worker> =
        workers.filter { worker ->
            (worker.name?.contains(query, ignoreCase = true) == true) ||
            worker.getCategoryEnum().displayName.contains(query, ignoreCase = true) ||
            (worker.services?.any { it.title?.contains(query, ignoreCase = true) == true } == true) ||
            (worker.location?.contains(query, ignoreCase = true) == true)
        }
}
