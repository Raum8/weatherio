package com.shenfeld.weatheriomvp.app.activities

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.edit
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.Constraints
import androidx.work.NetworkType
import com.shenfeld.weatherio.data.CityDto
import com.shenfeld.weatherio.utils.*
import com.shenfeld.weatheriomvp.R
import com.shenfeld.weatheriomvp.app.adapters.WeatherAdapter
import com.shenfeld.weatheriomvp.app.presenters.MainActivityPresenter
import com.shenfeld.weatheriomvp.app.views.MainActivityView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import moxy.MvpAppCompatActivity
import moxy.presenter.InjectPresenter
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class MainActivity : MvpAppCompatActivity(), MainActivityView {

    @InjectPresenter
    lateinit var mMainActivityPresenter: MainActivityPresenter
    private lateinit var disposable: Disposable
    private lateinit var sPref: SharedPreferences
    private lateinit var curCity: Cities

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e(MainActivity::class.java.simpleName, loadCity() + " ON CREATE")
        setupCity()
        days_recycler_view.layoutManager = LinearLayoutManager(this)
        iv_refresh_weather.setOnClickListener { refreshCityWeather() }
        mMainActivityPresenter.onLoadWeather(curCity.lat, curCity.lon)
        disposable = setupPeriodicRequest(curCity.lat, curCity.lon)
    }

    private fun setupCity() {
        when (loadCity()) {
            getString(R.string.moscow).toString() -> {
                curCity = Cities.MOSCOW
                //saveCity(getString(R.string.moscow))
            }
            getString(R.string.taganrog).toString() -> {
                curCity = Cities.TAGANROG
                //saveCity(getString(R.string.taganrog))
            }
            getString(R.string.krasnodar).toString() -> {
                curCity = Cities.KRASNODAR
                //saveCity(getString(R.string.krasnodar))
            }
            getString(R.string.rostov_on_don).toString() -> {
                curCity = Cities.ROSTOV_ON_DON
                //saveCity(getString(R.string.rostov_on_don))
            }
        }
    }

    private fun setupPeriodicRequest(
        lat: Double = ROSTOV_LAT,
        lon: Double = ROSTOV_LON
    ): Disposable {
        Log.e(MainActivity::class.java.simpleName, "SETUP PERIODIC REQ")
        return Observable.interval(
            10,
            TimeUnit.MINUTES
        ).observeOn(AndroidSchedulers.mainThread()).subscribe {
            mMainActivityPresenter.onLoadWeather(lat, lon)
            Log.d(MainActivity::class.java.simpleName, "OBSERVE ON MAIN ACTIVITY")
        }
    }

    fun saveCity(curCity: String) {
        Log.e(MainActivity::class.java.simpleName, "SAVE CITY")
        sPref = getPreferences(MODE_PRIVATE)
        sPref.edit {
            putString(getString(R.string.CURRENT_CITY), curCity)
            commit()
        }
    }

    fun loadCity(): String {
        Log.e(MainActivity::class.java.simpleName, "LOAD CITY")
        sPref = getPreferences(MODE_PRIVATE)
        return sPref.getString(getString(R.string.CURRENT_CITY), getString(R.string.rostov_on_don))
            .toString()
    }

    private fun refreshCityWeather() {
        when (tv_name_city.text) {
            getString(R.string.rostov_on_don) -> {
                mMainActivityPresenter.onLoadWeather()
            }
            getString(R.string.krasnodar) -> {
                mMainActivityPresenter.onLoadWeather(KRASNODAR_LAT, KRASNODAR_LON)
            }
            getString(R.string.moscow) -> {
                mMainActivityPresenter.onLoadWeather(MOSCOW_LAT, MOSCOW_LON)
            }
            getString(R.string.taganrog) -> {
                mMainActivityPresenter.onLoadWeather(TAGANROG_LAT, TAGANROG_LON)
            }
        }
        Log.e(MainActivity::class.java.simpleName, "REFRESH WEATHER")
    }

    @SuppressLint("SetTextI18n")
    override fun changeUI(dataWeather: CityDto) {
        days_recycler_view.adapter = WeatherAdapter(dataWeather.days, R.layout.list_item_day)
        tv_name_city.text = loadCity()
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
                saveCity(getString(R.string.moscow))
                cancelPeriodicRequest()
                mMainActivityPresenter.onLoadWeather(MOSCOW_LAT, MOSCOW_LON)
                disposable = setupPeriodicRequest(MOSCOW_LAT, MOSCOW_LON)
                tv_name_city.text = getString(R.string.moscow)
            }
            R.id.rostov -> {
                saveCity(getString(R.string.rostov_on_don))
                cancelPeriodicRequest()
                mMainActivityPresenter.onLoadWeather()
                disposable = setupPeriodicRequest()
                tv_name_city.text = getString(R.string.rostov_on_don)
            }
            R.id.krasnodar -> {
                saveCity(getString(R.string.krasnodar))
                cancelPeriodicRequest()
                mMainActivityPresenter.onLoadWeather(KRASNODAR_LAT, KRASNODAR_LON)
                disposable = setupPeriodicRequest(KRASNODAR_LAT, KRASNODAR_LON)
                tv_name_city.text = getString(R.string.krasnodar)
            }
            R.id.taganrog -> {
                saveCity(getString(R.string.taganrog))
                cancelPeriodicRequest()
                mMainActivityPresenter.onLoadWeather(TAGANROG_LAT, TAGANROG_LON)
                disposable = setupPeriodicRequest(TAGANROG_LAT, TAGANROG_LON)
                tv_name_city.text = getString(R.string.taganrog)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        cancelPeriodicRequest()
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        Log.e(MainActivity::class.java.simpleName, loadCity() + " ON STOP")
    }

    private fun cancelPeriodicRequest() {
        Log.e(MainActivity::class.java.simpleName, "DISPOSE")
        disposable.dispose()
    }
}