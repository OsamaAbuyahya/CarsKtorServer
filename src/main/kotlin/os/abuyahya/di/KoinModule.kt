package os.abuyahya.di

import org.koin.dsl.module
import os.abuyahya.repository.CarRepository
import os.abuyahya.repository.CarRepositoryImpl

val koinModule = module {
    single<CarRepository> {
        CarRepositoryImpl()
    }
}