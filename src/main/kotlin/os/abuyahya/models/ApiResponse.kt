package os.abuyahya.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val success: Boolean,
    val message: String? = null,
    val prevPage: Int? = null,
    val nextPage: Int? = null,
    val cars: List<Car> = emptyList(),
    val lastUpdated: Long? = null
)