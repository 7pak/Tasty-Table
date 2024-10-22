package com.abdts.search.data.di

import android.util.Log
import com.abdts.search.data.data_source.remote.SearchApi
import com.abdts.search.data.repository.SearchRepositoryImpl
import com.abdts.search.domain.repository.SearchRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val dataModule = module{
    single <HttpClient>{
        HttpClient {
            install(ContentNegotiation) {
                json(Json { isLenient = true; ignoreUnknownKeys = true })
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        Log.d("KtorFitLogger", "log: $message")
                    }

                }
                level = LogLevel.ALL
            }
        }
    }

    single <SearchApi>{
       SearchApi(httpClient = get(), url = "https://www.themealdb.com")
    }
    

    factory<SearchRepository> {
        SearchRepositoryImpl(get(),get())
    }
}
