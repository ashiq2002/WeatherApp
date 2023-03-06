package com.setbitzero.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.setbitzero.weatherapp.model.WeatherModel
import com.setbitzero.weatherapp.repository.WeatherRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val weatherRepo: WeatherRepo) : ViewModel() {

    fun getCurrentWeatherData(city_name:String = "Dhaka"):LiveData<WeatherModel>{
        viewModelScope.launch(Dispatchers.IO){
            weatherRepo.getCurrentWeatherData(city_name)
        }
        return weatherRepo.currentWeatherLiveData
    }
}