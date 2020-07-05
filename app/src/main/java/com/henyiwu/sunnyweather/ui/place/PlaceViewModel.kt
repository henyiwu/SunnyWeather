package com.henyiwu.sunnyweather.ui.place

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.henyiwu.sunnyweather.logic.Repository
import com.henyiwu.sunnyweather.logic.model.Place

class PlaceViewModel : ViewModel() {
    private val searchLiveData = MutableLiveData<String>()

    val placeList = ArrayList<Place>()

    /*
    /**错误示范，每次getData()都会获取新的对象，无法观察数据变化，所以使用switchMap()**/
    fun getData(query: String): LiveData<Result<*>> {
        return Repository.searchPlaces(query)
    }
    */

    /**
     * 使用switchMap()的原因：LiveData对象不是在ViewModel中创建的，而是调用另外的方法获取的，
     * switchMap()被调用后，将searchLiveData转为另一个可观察的LiveData对象placeLiveData，
     * 如果不用switchMap()，则Repository.searchPlaces(query)每次返回的都是新的LiveData对象，
     * Activity或Fragment中无法观察其数据变化
     */
    val placeLiveData = Transformations.switchMap(searchLiveData) {query ->
        Repository.searchPlaces(query)
    }

    /**
     * 一旦调用搜索城市函数，searchLiveData的数据发生变化，placeLiveData同样发生变化，
     * placeLiveData数据发生变化后，observe()函数的回调执行
     */
    fun searchPlaces(query: String) {
        /**
         * searchLiveData的数据发生变化，观察searchLiveData的switchMap()会被执行，
         * 调用我们编写的switchMap()方法。
         */
        searchLiveData.value = query
    }

    fun savePlace(place: Place) = Repository.savePlace(place)

    fun getSavedPlace() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()
}