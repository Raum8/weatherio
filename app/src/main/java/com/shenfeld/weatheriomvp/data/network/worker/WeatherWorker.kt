package com.shenfeld.weatheriomvp.data.network.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.shenfeld.weatherio.utils.ROSTOV_LAT
import com.shenfeld.weatherio.utils.ROSTOV_LON
import com.shenfeld.weatheriomvp.app.presenters.MainActivityPresenter
import java.lang.Exception

class WeatherWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {
    private var dataPresenter: MainActivityPresenter = MainActivityPresenter()

    override fun doWork(): Result {
        val lat = inputData.getDouble("lat", ROSTOV_LAT)
        val lon = inputData.getDouble("lon", ROSTOV_LON)
        dataPresenter.onLoadWeather(lat, lon)
        Log.e("WORKER", "WORKER START")
        return Result.success()
    }
}