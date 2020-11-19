package com.shenfeld.weatherio.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun getSimpleDataForm(dataUnix: Long, dateFormat: String = DATE_FORMAT): String =
    SimpleDateFormat(dateFormat).format(Date(dataUnix))
