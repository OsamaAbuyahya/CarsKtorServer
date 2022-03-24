package os.abuyahya

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.koin.java.KoinJavaComponent
import os.abuyahya.models.ApiResponse
import os.abuyahya.models.Car
import os.abuyahya.others.Constant.NEXT_PAGE_KEY
import os.abuyahya.others.Constant.PREVIOUS_PAGE_KEY
import os.abuyahya.plugins.configureRouting
import os.abuyahya.repository.CarRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {

    private val carRepository: CarRepository by KoinJavaComponent.inject(CarRepository::class.java)

    @Test
    fun testRoot() {
        withTestApplication({ configureRouting() }) {
            handleRequest(HttpMethod.Get, "/").apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("Welcome to Cars API", response.content)
            }
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun `access all cars endpoint, assert correct information`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "api/v1/cars").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )

                val expected = ApiResponse(
                    success = true,
                    message = "OK",
                    prevPage = null,
                    nextPage = 2,
                    cars = carRepository.page1
                )
                val actual = Json.decodeFromString<ApiResponse>(response.content.toString())

                assertEquals(
                    expected = expected,
                    actual = actual
                )
            }
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun `access all cars endpoint, query all pages, assert correct information`() {
        withTestApplication(moduleFunction = Application::module) {
            val pages = 1..3
            val cars = listOf(
                carRepository.page1,
                carRepository.page2,
                carRepository.page3
            )

            pages.forEach { page ->
                handleRequest(HttpMethod.Get, "api/v1/cars?page=$page").apply {
                    println("Current Page: $page")
                    assertEquals(
                        expected = HttpStatusCode.OK,
                        actual = response.status()
                    )

                    val expected = ApiResponse(
                        success = true,
                        message = "OK",
                        prevPage = calculatePage(page)[PREVIOUS_PAGE_KEY],
                        nextPage = calculatePage(page)[NEXT_PAGE_KEY],
                        cars = cars[page - 1]
                    )
                    val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
                    println("Expected: $expected")
                    println("Actual: $actual")

                    assertEquals(
                        expected = expected,
                        actual = actual
                    )
                }
            }
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun `access all cars endpoint, query non existing page number, assert error`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "api/v1/cars?page=9").apply {
                assertEquals(
                    expected = HttpStatusCode.NotFound,
                    actual = response.status()
                )

                val expected = ApiResponse(
                    success = false,
                    message = "Cars Not Found!"
                )
                val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
                println("Expected: $expected")
                println("Actual: $actual")

                assertEquals(
                    expected = expected,
                    actual = actual
                )
            }
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun `access all cars endpoint, query invalid page number, assert error`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "api/v1/cars?page=hhh").apply {
                assertEquals(
                    expected = HttpStatusCode.BadRequest,
                    actual = response.status()
                )

                val expected = ApiResponse(
                    success = false,
                    message = "Only Numbers Allowed!"
                )
                val actual = Json.decodeFromString<ApiResponse>(response.content.toString())
                println("Expected: $expected")
                println("Actual: $actual")

                assertEquals(
                    expected = expected,
                    actual = actual
                )
            }
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun `access search cars endpoint, assert single car result`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "api/v1/search?name=gmc").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )

                val actual = Json.decodeFromString<ApiResponse>(response.content.toString()).cars.size
                println("Expected Size: 1")
                println("Actual Size: $actual")

                assertEquals(
                    expected = 1,
                    actual = actual
                )
            }
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun `access search cars endpoint, query car name, assert multiple cars result`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "api/v1/search?name=fo").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )

                val actual = Json.decodeFromString<ApiResponse>(response.content.toString()).cars.size
                println("Expected Size: 2")
                println("Actual Size: $actual")

                assertEquals(
                    expected = 2,
                    actual = actual
                )
            }
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun `access search cars endpoint, query an empty text, assert empty list as a result`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "api/v1/search?name=").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )

                val actual = Json.decodeFromString<ApiResponse>(response.content.toString()).cars
                println("Expected Size: ${emptyList<Car>()}")
                println("Actual Size: $actual")

                assertEquals(
                    expected = emptyList(),
                    actual = actual
                )
            }
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun `access search cars endpoint, query non existing car, assert empty list as a result`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "api/v1/search?name=non existing").apply {
                assertEquals(
                    expected = HttpStatusCode.OK,
                    actual = response.status()
                )

                val actual = Json.decodeFromString<ApiResponse>(response.content.toString()).cars
                println("Expected Size: ${emptyList<Car>()}")
                println("Actual Size: $actual")

                assertEquals(
                    expected = emptyList(),
                    actual = actual
                )
            }
        }
    }

    @ExperimentalSerializationApi
    @Test
    fun `access non existing endpoint, assert not found`() {
        withTestApplication(moduleFunction = Application::module) {
            handleRequest(HttpMethod.Get, "/unknown").apply {
                assertEquals(
                    expected = HttpStatusCode.NotFound,
                    actual = response.status()
                )
                assertEquals(
                    expected = "Page Not Found!",
                    actual = response.content
                )
            }
        }
    }


    private fun calculatePage(page: Int): Map<String, Int?> {
        var prevPage: Int? = page
        var nextPage: Int? = page

        if (page in 1..2)
            nextPage = nextPage?.plus(1)

        if (page in 2..3)
            prevPage = prevPage?.minus(1)

        if (page == 1)
            prevPage = null

        if (page == 3)
            nextPage = null

        return mapOf(PREVIOUS_PAGE_KEY to prevPage, NEXT_PAGE_KEY to nextPage)
    }

}