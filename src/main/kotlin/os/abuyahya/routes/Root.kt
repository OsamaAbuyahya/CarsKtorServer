package os.abuyahya.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import javax.naming.AuthenticationException

fun Route.root() {
    get("/") {
        call.respond(
            status = HttpStatusCode.OK,
            "Welcome to Cars API"
        )
    }
}