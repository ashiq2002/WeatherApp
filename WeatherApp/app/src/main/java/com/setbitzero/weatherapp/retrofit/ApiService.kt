package com.setbitzero.weatherapp.retrofit

import com.setbitzero.weatherapp.model.WeatherModel
import com.setbitzero.weatherapp.retrofit.key.ApiKey
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("data/2.5/weather?")
    suspend fun getCurrentWeather(@Query("q") city_name:String = "Dhaka",
                                  @Query("appid") api_key:String = ApiKey.API_KEY):Response<WeatherModel>
}