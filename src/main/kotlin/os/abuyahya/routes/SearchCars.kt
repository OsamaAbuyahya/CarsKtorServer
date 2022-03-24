package os.abuyahya.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import os.abuyahya.models.ApiResponse
import os.abuyahya.repository.CarRepository

fun Route.searchCars() {

    val carRepository: CarRepository by inject()

    get("api/v1/search") {
        val name = call.request.queryParameters["name"]

        val apiResponse = carRepository.searchCars(name)
        call.respond(
            message = apiResponse,
            status = HttpStatusCode.OK
        )
    }
}