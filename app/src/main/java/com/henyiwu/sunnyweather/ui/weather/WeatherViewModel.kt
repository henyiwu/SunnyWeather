package com.henyiwu.sunnyweather.ui.weather

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.henyiwu.sunnyweather.logic.Repository
import com.henyiwu.sunnyweather.logic.model.Location

class WeatherViewModel: ViewModel() {

    private final val TAG: String = "WeatherViewModel"

    private val locationLiveData = MediatorLiveData<Location>()

    var locationLng = ""

    var locationLat = ""

    var placeName = ""

    val weatherLiveData = Transformations.switchMap(locationLiveData) {location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }
}