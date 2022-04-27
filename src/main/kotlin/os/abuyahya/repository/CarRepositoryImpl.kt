package os.abuyahya.repository

import os.abuyahya.models.ApiResponse
import os.abuyahya.models.Car
import os.abuyahya.others.Constant.NEXT_PAGE_KEY
import os.abuyahya.others.Constant.PREVIOUS_PAGE_KEY

class CarRepositoryImpl : CarRepository {
    override val cars: Map<Int, List<Car>> by lazy {
        mapOf(
            1 to page1,
            2 to page2,
            3 to page3
        )
    }

    override val page1 = listOf(
        Car(
            1, "Honda Pilot", "/images/honda_pilot.jpg", 38805, "Black",
            "Honda's three-row SUV is within top 25 thanks to its strong numbers that were up 16 percent over last year.",
            listOf("Fuel Economy", "Interior", "Comfort", "Cargo", "Driver-Assistance"), 4.3
        ),
        Car(
            2, "Ford Escape", "/images/ford_escape.jpg", 27755, "Red",
            "Now that its more stylish and exciting Bronco Sport sibling is on sale, it's no wonder that the somewhat anonymous Escape has fallen in the ranks.",
            listOf("Range", "Charging", "Battery Life"), 4.2
        ),
        Car(
            3, "Hyundai Tucson", "/images/hyundai_tucson.jpg", 26195, "Blue",
            "The new 2022 version of Hyundai's compact SUV has a bold look, and it seems customers are loving it.",
            listOf("Range", "Charging", "Battery Life"), 4.4
        )
    )
    override val page2 = listOf(
        Car(
            4, "Nissan Rogue", "/images/nissan_rogue.jpg", 27875, "Blue",
            "The redesigned 2021 Rogue is far better than its predecessor, and the numbers are reflecting a similar improvement so far this year.",
            listOf("Range", "Charging", "Battery Life"), 4.8
        ),
        Car(
            5, "Toyota RAV4", "/images/toyota_rav4.jpg", 27740, "White",
            "Ever since it achieved the title of bestselling non-truck in the country, the RAV4 has been extending its lead over its compact-crossover rivals.",
            listOf("Range", "Charging", "Battery Life"), 4.0
        ),
        Car(
            6, "Chevrolet Silverado", "/images/chevrolet_silverado.jpg", 32695, "Red",
            "Brand loyalty is big when it comes to full-size trucks, and the 2022 Chevy Silverado gives loyalists things to get excited about.",
            listOf("Range", "Charging", "Battery Life"), 4.7
        )
    )
    override val page3 = listOf(
        Car(
            7, "Ford Explore", "/images/ford_explore.jpg", 35040, "White",
            "The Explorer and the Toyota Highlander were duking it out for the three-row-SUV sales crown earlier this year.",
            listOf("Range", "Charging", "Battery Life"), 4.6
        ),
        Car(
            8, "GMC Sierra", "/images/gmc_sierra.jpg", 33495, "Black",
            "Sales of both the light-duty and heavy-duty Sierra models fell this year, with the 1500 model down 0.5 percent and the HD trucks down 4 percent.",
            listOf("Towing and Payload Capacity", "Fuel Economy", "Safety"), 4.8
        ),
        Car(
            9, "Jeep Grand Cherokee", "/images/jeep_grand_cherokee.jpg", 37330, "Selver",
            "A new generation of the Grand Cherokee is now on sale, starting with the three-row L model, but the current two-row version is still available.",
            listOf("Warranty and Maintenance Coverage", "Fuel Economy", "Safety"), 4.1
        )
    )

    override suspend fun getAllCars(page: Int): ApiResponse {
        return ApiResponse(
            success = true,
            message = "OK",
            prevPage = calculatePage(page)[PREVIOUS_PAGE_KEY],
            nextPage = calculatePage(page)[NEXT_PAGE_KEY],
            cars[page]!!,
            lastUpdated = System.currentTimeMillis()
        )
    }

    override suspend fun searchCars(name: String?): ApiResponse {
        return ApiResponse(
            success = true,
            message = "OK",
            cars = findCars(query = name)
        )
    }

    private fun findCars(query: String?): List<Car> {
        val founded = mutableListOf<Car>()

        return if (!query.isNullOrEmpty()) {
            cars.forEach { (_, cars) ->
                cars.forEach { car ->
                    if (car.name.lowercase().contains(query.lowercase()))
                        founded.add(car)
                }
            }
            founded
        } else {
            emptyList()
        }
    }

    private fun calculatePage(page: Int): Map<String, Int?> = mapOf(
        PREVIOUS_PAGE_KEY to if (page in 2..3) page.minus(1) else null,
        NEXT_PAGE_KEY to if (page in 1..2) page.plus(1) else null
    )
}
