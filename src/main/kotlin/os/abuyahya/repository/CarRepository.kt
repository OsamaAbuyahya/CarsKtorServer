package os.abuyahya.repository

import os.abuyahya.models.ApiResponse
import os.abuyahya.models.Car

interface CarRepository {

    val cars: Map<Int, List<Car>>

    val page1: List<Car>
    val page2: List<Car>
    val page3: List<Car>

    suspend fun getAllCars(page: Int = 1): ApiResponse
    suspend fun searchCars(name: String?): ApiResponse
}