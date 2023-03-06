package com.setbitzero.weatherapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.setbitzero.weatherapp.repository.WeatherRepo

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val weatherRepo: WeatherRepo):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(weatherRepo) as T
    }
}