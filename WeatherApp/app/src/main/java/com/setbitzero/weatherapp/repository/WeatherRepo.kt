package com.setbitzero.weatherapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.setbitzero.weatherapp.model.WeatherModel
import com.setbitzero.weatherapp.retrofit.ApiService

class WeatherRepo(private val apiService: ApiService){
    private val currentWeatherData = MutableLiveData<WeatherModel>()
    val currentWeatherLiveData:LiveData<WeatherModel> get() = currentWeatherData
    //---------------------------------------x------------------------------------------

    suspend fun getCurrentWeatherData(city_name:String = "Dhaka"){
        val response = apiService.getCurrentWeather(city_name = city_name)

        if(response.isSuccessful && response.body() != null){
            currentWeatherData.postValue(response.body())
        }
    }
}