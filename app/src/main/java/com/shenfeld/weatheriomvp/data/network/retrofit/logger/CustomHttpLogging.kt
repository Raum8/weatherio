package com.shenfeld.weatheriomvp.data.network.retrofit.logger

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import okhttp3.logging.HttpLoggingInterceptor

class CustomHttpLogging : HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        val TAG = "OkHttp"
        if (!message.startsWith("{")) {
            Log.d(TAG, message)
        } else {
            try {
                val stringJson = GsonBuilder()
                    .setPrettyPrinting()
                    .create()
                    .toJson(JsonParser().parse(message))
                Log.d(TAG, stringJson)
            } catch (e: JsonSyntaxException) {
                Log.d(TAG, message)
            }
        }
    }
}