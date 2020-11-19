package com.shenfeld.weatherio.data

import com.google.gson.annotations.SerializedName

class CityDto (
    @SerializedName("daily") val days: List<DayDto>
)