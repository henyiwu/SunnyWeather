package com.henyiwu.sunnyweather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit构建器
 */
object ServiceCreator {

    /**服务器基本域名**/
    private const val BASE_URL = "https://api.caiyunapp.com"

    /**创建Retrofit的Builder**/
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**创建Retrofit对象**/
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    /**利用泛型实化创建**/
    inline fun <reified T> create(): T = create(T::class.java)
}