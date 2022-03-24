package os.abuyahya.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject
import os.abuyahya.models.ApiResponse
import os.abuyahya.repository.CarRepository

fun Route.getAllCars() {

    val carRepository: CarRepository by inject()

    get("api/v1/cars") {
        try {
            val page = call.request.queryParameters["page"]?.toInt() ?: 1
            require(page in 1..3)

            val cars = carRepository.getAllCars(page = page)

            call.respond(
                message = cars,
                status = HttpStatusCode.OK
            )
        } catch (e: NumberFormatException) {
            call.respond(
                message = ApiResponse(success = false, message = "Only Numbers Allowed!"),
                status = HttpStatusCode.BadRequest
            )
        } catch (e: IllegalArgumentException) {
            call.respond(
                message = ApiResponse(success = false, message = "Cars Not Found!"),
                status = HttpStatusCode.NotFound
            )
        }

    }
}