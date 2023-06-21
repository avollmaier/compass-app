package at.avollmaier.compassapp.service

import at.avollmaier.compassapp.models.WeatherDetailData
import at.avollmaier.compassapp.models.WeatherResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.jackson.jackson

class WeatherApiService {
    private val client = HttpClient(CIO) {
        install(DefaultRequest) {
            headers.append("Content-Type", "application/json")
        }

        install(ContentNegotiation) {
            jackson()
        }
    }

    suspend fun getWeatherData(lat: Double, lon: Double): WeatherResponse {
        return try {
            val url =
                "https://api.openweathermap.org/data/2.5/weather?lat=$lat&lon=$lon&appid=eae32a65be293a2bc15b59cb86608b96"
            client.get(url).body()
        }catch (e: Exception) {
            WeatherResponse(WeatherDetailData(0.0 , 0))
        }
    }

}