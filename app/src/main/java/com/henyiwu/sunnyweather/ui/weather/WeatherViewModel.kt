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

    /**因为LiveData对象是调用其他类获取的，每次获得的都是新的对象，所以要通过switchMap转换**/
    val weatherLiveData = Transformations.switchMap(locationLiveData) {location ->
        Repository.refreshWeather(location.lng, location.lat)
    }

    /**locationLiveData的数据发生变化后，观察locationLiveData的switchMap()会执行**/
    fun refreshWeather(lng: String, lat: String) {
        locationLiveData.value = Location(lng, lat)
    }
}