package os.abuyahya.plugins

import io.ktor.application.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.modules
import org.koin.logger.slf4jLogger
import os.abuyahya.di.koinModule

fun Application.configureKoin() {
    install(Koin) {
        // set level to ERROR to Solve this 'double kotlin.time.Duration.toDouble-impl(long, java.util.concurrent.TimeUnit)'
        slf4jLogger(level = org.koin.core.logger.Level.ERROR)
        modules(koinModule)
    }
}