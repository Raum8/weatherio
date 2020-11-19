package com.shenfeld.weatheriomvp.data.network

import com.shenfeld.weatherio.data.CityDto
import retrofit2.Call

interface WeatherProvider {
    fun fetchWeather(lat: Double, lon: Double): Call<CityDto>
}
