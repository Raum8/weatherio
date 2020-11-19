package com.shenfeld.weatheriomvp.app.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shenfeld.weatherio.data.DayDto
import com.shenfeld.weatherio.utils.DATE_OF_WEEK
import com.shenfeld.weatherio.utils.DAY_OF_WEEK
import com.shenfeld.weatherio.utils.getSimpleDataForm
import com.shenfeld.weatheriomvp.R
import kotlin.math.roundToInt

class WeatherAdapter(
    private val days: List<DayDto>,
    private val rowLayout: Int
) : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    class WeatherViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        internal var dayOfWeek = v.findViewById<TextView>(R.id.tv_day_of_week)
        internal var dateOfWeek = v.findViewById<TextView>(R.id.tv_date_of_week)
        internal var dayTemp = v.findViewById<TextView>(R.id.tv_day_temperature)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(rowLayout, parent, false)
        return WeatherViewHolder(view)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val current = days[position]
        holder.dayOfWeek.text = getSimpleDataForm(current.dt.toLong() * 1000, DAY_OF_WEEK)
        holder.dateOfWeek.text = getSimpleDataForm(current.dt.toLong() * 1000, DATE_OF_WEEK)
        holder.dayTemp.text = "${current.temp.day.roundToInt()}°"
    }

    override fun getItemCount(): Int {
        return days.size
    }
}