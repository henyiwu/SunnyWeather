package com.henyiwu.sunnyweather

import android.app.Application
import android.content.Context

class SunnyWeatherApplication : Application() {
    companion object {
        /**天气请求令牌**/
        const val TOKEN = "rYnz0NAKVHF2fGvB"
        /**全局context**/
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}