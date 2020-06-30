package com.henyiwu.sunnyweather.logic.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 统一的网络数据源访问入口，对所有网络请求API进行封装
 */
object SunnyWeatherNetwork {

    private val TAG: String = "SunnyWeatherNetwork"

    private val weatherService = ServiceCreator.create<WeatherService>()

    private val placeService = ServiceCreator.create<PlaceService>()

    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    /**
     * Call的拓展函数，使用suspend修饰使其成为一个挂起函数，可在函数中开启协程
     */
    private suspend fun <T> Call<T>.await(): T {
        // 这里没被调用到
        /**
         * suspendCoroutine()函数必须在协程作用域或挂起函数中才能使用，将当前协程立刻挂起
         */
        return suspendCoroutine {continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) {
                        /**恢复被挂起的协程，并传入服务器响应的数据，该值成为suspendCoroutine()的返回值**/
                        continuation.resume(body)
                    } else {
                        /**恢复被挂起的协程，并传入具体的异常原因**/
                        continuation.resumeWithException(RuntimeException("response body is null"))
                    }
                }

            })
        }
    }

    suspend fun getDailyWeather(lng: String, lat: String) =
        weatherService.getDailyWeather(lng, lat).await()

    suspend fun getRealtimeWeather(lng: String, lat: String) =
        weatherService.getRealtimeWeather(lng, lat).await()

}