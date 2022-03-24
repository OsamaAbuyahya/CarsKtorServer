package os.abuyahya.plugins

import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import os.abuyahya.routes.getAllCars
import os.abuyahya.routes.root
import os.abuyahya.routes.searchCars

fun Application.configureRouting() {

    routing {
        root()
        getAllCars()
        searchCars()

        static("/images") {
            resources("images")
        }
    }
}
