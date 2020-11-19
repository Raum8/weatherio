package com.shenfeld.weatheriomvp.app.views

import com.shenfeld.weatherio.data.CityDto
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface MainActivityView: MvpView {
    fun changeUI(dataWeather: CityDto)
    fun showError(mesg: String)
}