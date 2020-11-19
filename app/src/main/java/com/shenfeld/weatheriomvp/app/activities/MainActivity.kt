package com.shenfeld.weatheriomvp.app.activities

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.*
import com.shenfeld.weatherio.data.CityDto
import com.shenfeld.weatherio.utils.Cities
import com.shenfeld.weatherio.utils.Cities.*
import com.shenfeld.weatheriomvp.R
import com.shenfeld.weatheriomvp.app.adapters.WeatherAdapter
import com.shenfeld.weatheriomvp.app.presenters.MainActivityPresenter
import com.shenfeld.weatheriomvp.app.views.MainActivityView
import com.shenfeld.weatheriomvp.data.network.worker.WeatherWorker
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

class MainActivity : MvpAppCompatActivity(), MainActivityView {

    @InjectPresenter
    lateinit var mMainActivityPresenter: MainActivityPresenter

    private val constraints =
        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        days_recycler_view.layoutManager = LinearLayoutManager(this)
        setupWorker()
        iv_refresh_weather.setOnClickListener {
            refreshCityWeather()
        }
    }

    private fun setupWorker(city: Cities = ROSTOV_ON_DON) {
        val coordCity: Data =
            Data.Builder()
                .putDouble("lat", city.lat)
                .putDouble("lon", city.lon).build()
        val requestWeatherPeriodic =
            PeriodicWorkRequestBuilder<WeatherWorker>(10, TimeUnit.SECONDS)
                .setConstraints(constraints)
                .addTag("WORK")
                .setInputData(coordCity)
                .build()
        WorkManager.getInstance(applicationContext).enqueue(requestWeatherPeriodic)
    }

    private fun cancelWorker() {
        WorkManager.getInstance(applicationContext).cancelAllWorkByTag("WORK")
    }

    private fun refreshCityWeather() {
        when (tv_name_city.text) {
            getString(R.string.rostov_on_don) -> {
                cancelWorker()
                setupWorker(ROSTOV_ON_DON)
            }
            getString(R.string.krasnodar) -> {
                cancelWorker()
                setupWorker(KRASNODAR)
            }
            getString(R.string.moscow) -> {
                cancelWorker()
                setupWorker(MOSCOW)
            }
            getString(R.string.taganrog) -> {
                cancelWorker()
                setupWorker(TAGANROG)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun changeUI(dataWeather: CityDto) {
        days_recycler_view.adapter = WeatherAdapter(dataWeather.days, R.layout.list_item_day)
        tv_current_main_weather.text =
            "${dataWeather.days[0].weather[0].main}, ${dataWeather.days[0].temp.day.roundToInt()}°"
        tv_current_date.text = "Humidity: ${dataWeather.days[0].humidity}%"
        val sunset = dataWeather.days[0].sunset.toLong() * 1000
        val sunrise = dataWeather.days[0].sunrise.toLong() * 1000
        val curTime = Date().time
        if (curTime in sunrise until sunset)
            iv_current_times_of_day.setImageResource(R.drawable.day)
        else
            iv_current_times_of_day.setImageResource(R.drawable.night)
    }

    override fun showError(mesg: String) {
        Toast.makeText(applicationContext, mesg, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.moscow -> {
                cancelWorker()
                tv_name_city.text = getString(R.string.moscow)
                setupWorker(MOSCOW)
            }
            R.id.rostov -> {
                cancelWorker()
                tv_name_city.text = getString(R.string.rostov_on_don)
                setupWorker()
            }
            R.id.krasnodar -> {
                cancelWorker()
                tv_name_city.text = getString(R.string.krasnodar)
                setupWorker(KRASNODAR)
            }
            R.id.taganrog -> {
                cancelWorker()
                tv_name_city.text = getString(R.string.taganrog)
                setupWorker(TAGANROG)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        cancelWorker()
        super.onDestroy()
    }
}