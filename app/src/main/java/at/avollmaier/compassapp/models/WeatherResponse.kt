package at.avollmaier.compassapp.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
class WeatherResponse() {
    lateinit var main: WeatherDetailData

    constructor(main: WeatherDetailData) : this() {
        this.main = main
    }
}
