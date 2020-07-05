package com.henyiwu.sunnyweather.logic

import android.content.Context
import android.util.Log
import androidx.lifecycle.liveData
import com.henyiwu.sunnyweather.logic.dao.PlaceDao
import com.henyiwu.sunnyweather.logic.model.Place
import com.henyiwu.sunnyweather.logic.model.Weather
import com.henyiwu.sunnyweather.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.lang.Exception
import java.lang.RuntimeException
import kotlin.coroutines.CoroutineContext

/**仓库层的统一封装入口**/
object Repository {

    private final val TAG: String = "Repository"

    /**
     * 该函数用于搜索城市数据
     * 为了能够将异步获取的数据以响应式的方式通知给上一层，返回一个liveData对象
     * liveData()函数为lifecycle-livedata-ktx提供，带三个参数，其中第二个参数有默认值，
     * 第三个参数是lambda表达式，提供了一个挂起函数的上下文，所以可以在代码块中调用挂起函数。
     */
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
        /**如果响应状态是ok，则使用kotlin内置的Result.success()方法包装获取的城市数据列表**/
        /**如果响应状态是ok，则使用kotlin内置的Result.success()方法包装获取的城市数据列表**/
        if (placeResponse.status == "ok") {
            val places = placeResponse.places
            Result.success(places)
        } else {
            /**响应状态失败，使用Result.failure()方法包装一个异常信息**/
            /**响应状态失败，使用Result.failure()方法包装一个异常信息**/
            Result.failure(RuntimeException("response status is ${placeResponse.status}"))
        }
        /**将包装结果发射出去，类似于调用LiveData的setValue()方法通知数据的变化**/
        /**将包装结果发射出去，类似于调用LiveData的setValue()方法通知数据的变化**/
    }

    /**
     * 根据经纬度获取天气信息
     */
    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            /**获取天气信息**/
            /**获取天气信息**/
            val deferredRealtime = async {
                SunnyWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            /**获取未来天气信息**/
            /**获取未来天气信息**/
            val deferredDaily = async {
                SunnyWeatherNetwork.getDailyWeather(lng, lat)
            }
            val dailyResponse = deferredDaily.await()
            val realtimeResponse = deferredRealtime.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather =
                    Weather(realtimeResponse.result.realtime, dailyResponse.result.daily)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    /**
     * 该函数为高阶函数
     * @param block 原本回调到lambda表达式中，代码就没有挂起函数的上下文了，声明为suspend，
     * 表示传入的lambda表达式也是拥有挂起函数上下文的。
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceShaved()
}