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

    override fun doWork(): Result {
        return Result.success()
    }
}