package at.avollmaier.compassapp.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class WeatherDetailData ( ) {
    private var humidity: Int = 0
    private var temp: Double = 0.0

    constructor(temp: Double, humidity: Int) : this() {
        this.temp = temp
        this.humidity = humidity
    }

    fun getHumidity(): Int {
        return humidity
    }

    fun getTemp(): Double {
        return temp
    }
}