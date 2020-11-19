package com.shenfeld.weatheriomvp.data.network.retrofit

import com.shenfeld.weatheriomvp.data.network.retrofit.logger.CustomHttpLogging
import com.shenfeld.weatheriomvp.data.network.services.WeatherService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {
    companion object {
        private val baseUrl = "http://api.openweathermap.org/"
        private fun getOkHttpInstance(): OkHttpClient {
            val interceptor =
                HttpLoggingInterceptor(CustomHttpLogging()).setLevel(HttpLoggingInterceptor.Level.BODY)
            return OkHttpClient.Builder().addInterceptor(interceptor).build()
        }

        private fun getRetrofitInstance(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
               // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpInstance())
                .build()
        }

        fun getWeatherService() = getRetrofitInstance().create(WeatherService::class.java)
    }
}