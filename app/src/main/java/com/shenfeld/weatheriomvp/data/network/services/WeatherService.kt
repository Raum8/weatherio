package com.shenfeld.weatheriomvp.data.network.services

import com.shenfeld.weatherio.data.CityDto
import io.reactivex.Single
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("data/2.5/onecall")
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String,
        @Query("exclude") exclude: String,
        @Query("units") units: String
    ): Call<CityDto>
}