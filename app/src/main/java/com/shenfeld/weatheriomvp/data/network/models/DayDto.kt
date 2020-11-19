package com.shenfeld.weatherio.data

import com.google.gson.annotations.SerializedName

class DayDto(
    @SerializedName("dt") val dt : Int,
    @SerializedName("sunrise") val sunrise : Int,
    @SerializedName("sunset") val sunset : Int,
    @SerializedName("temp") val temp : TempDto,
    @SerializedName("weather") val weather : List<WeatherDto>,
    @SerializedName("humidity") val humidity : Int
)