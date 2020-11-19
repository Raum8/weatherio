package com.shenfeld.weatheriomvp.app.presenters

import android.annotation.SuppressLint
import androidx.work.WorkManager
import com.shenfeld.weatherio.data.CityDto
import com.shenfeld.weatherio.utils.Cities
import com.shenfeld.weatherio.utils.ROSTOV_LAT
import com.shenfeld.weatherio.utils.ROSTOV_LON
import com.shenfeld.weatheriomvp.app.views.MainActivityView
import com.shenfeld.weatheriomvp.data.network.WeatherProvider
import com.shenfeld.weatheriomvp.data.network.impl.WeatherProviderImpl
import com.shenfeld.weatheriomvp.data.network.worker.WeatherWorker
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import moxy.InjectViewState
import moxy.MvpPresenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@InjectViewState
class MainActivityPresenter : MvpPresenter<MainActivityView>() {
    private var dataProvider: WeatherProvider = WeatherProviderImpl()

    fun onLoadWeather(lat: Double = ROSTOV_LAT, lon: Double = ROSTOV_LON) {
        dataProvider.fetchWeather(lat, lon).enqueue(object : Callback<CityDto> {
            override fun onResponse(call: Call<CityDto>, response: Response<CityDto>) {
                if (response.isSuccessful) {
                    val dataResponse = response.body()
                    if (dataResponse != null)
                        viewState.changeUI(dataResponse)
                    else
                        viewState.showError("DATA IS EMPTY")
                } else {
                    viewState.showError("RESPONSE CODE: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<CityDto>, t: Throwable) {
                viewState.showError(t.message.toString())
            }
        })
    }
}