package os.abuyahya.models

import kotlinx.serialization.Serializable

@Serializable
data class Car(
    var id: Int,
    var name: String,
    var image: String,
    var price: Int,
    var color: String,
    var about: String,
    var features: List<String>,
    var rating: Double
)
