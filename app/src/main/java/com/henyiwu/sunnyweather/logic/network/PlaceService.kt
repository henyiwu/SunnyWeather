package com.henyiwu.sunnyweather.logic.network

import com.henyiwu.sunnyweather.SunnyWeatherApplication
import com.henyiwu.sunnyweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**访问彩云天气城市搜索API的Retrofit接口**/
interface PlaceService {
    /**
     * 打上Retrofit的@GET注解，调用searchPlaces()方法时，Retrofit会自动发起一条GET请求
     */
    @GET("v2/place?token=${SunnyWeatherApplication.TOKEN}&lang=zh_CN")
    /**
     * 返回值声明为Call<PlaceResponse>，Retrofit会将服务器返回的JSON数据自动解析为PlaceResponse对象
     */
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}