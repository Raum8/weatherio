package com.shenfeld.weatheriomvp.data.network.impl

import com.shenfeld.weatherio.data.CityDto
import com.shenfeld.weatherio.utils.*
import com.shenfeld.weatheriomvp.data.network.WeatherProvider
import com.shenfeld.weatheriomvp.data.network.retrofit.RetrofitFactory
import io.reactivex.Single
import retrofit2.Call

class WeatherProviderImpl : WeatherProvider {
    override fun fetchWeather(lat: Double, lon: Double): Call<CityDto> =
        RetrofitFactory.getWeatherService()
            .getWeather(lat = lat, lon = lon, appId = API_KEY, exclude = EXCLUDE, units = UNITS)
}