package com.setbitzero.weatherapp

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.setbitzero.weatherapp.databinding.ActivityMainBinding
import com.setbitzero.weatherapp.repository.WeatherRepo
import com.setbitzero.weatherapp.retrofit.RetroClient
import com.setbitzero.weatherapp.service.MyReceiver
import com.setbitzero.weatherapp.viewModel.MainViewModel
import com.setbitzero.weatherapp.viewModel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var alarmManager: AlarmManager
    private var _isVisible = false
    companion object{
        private const val DB_NAME = "CITY_NAME"
        private const val REQ_CODE = 100
        private const val KEY = "CityName"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val weatherRepo = WeatherRepo(RetroClient.apiService)
        mainViewModel = ViewModelProvider(this, MainViewModelFactory(weatherRepo))[MainViewModel::class.java]

        if(getCityName().isEmpty()){ // if City name is empty then save default data
            saveCityName("Dhaka")
        }

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize alarManager
        alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        //get weather data
        mainViewModel.getCurrentWeatherData(getCityName()).observe(this){ weather->
            Log.wtf("Response", weather.weather.toString())
            if(weather == null){
                binding.progressBar.visibility = View.VISIBLE
            }else{
                binding.progressBar.visibility = View.GONE
                binding.weather = weather
            }
        }

        binding.searchButton.background = null
        //search weather data by city name
        binding.searchButton.setOnClickListener {
            val city = binding.searchWeather.text.toString()
            if(city.isNotEmpty()){
                saveCityName(city)
                mainViewModel.getCurrentWeatherData(getCityName()).observe(this){ weather->
                    if(weather == null){
                        binding.progressBar.visibility = View.VISIBLE
                    }else{
                        binding.progressBar.visibility = View.GONE
                        binding.weather = weather
                    }
                }
            }
            searchView()

        }


    }

    //custom search view
    private fun searchView(){
        if(_isVisible){
            binding.searchWeather.visibility = View.GONE
            binding.searchWeather.setText("")
            binding.title.visibility = View.VISIBLE
            _isVisible = false
            return
        }
        binding.searchWeather.visibility = View.VISIBLE
        binding.title.visibility = View.GONE
        _isVisible = true
    }

    //save city name
    @SuppressLint("CommitPrefEdits")
    private fun saveCityName(cityName: String){
        val pref = getSharedPreferences("CITY_NAME", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear()
        editor.putString(KEY, cityName)
        editor.apply()
    }

    //get city name
    private fun getCityName(): String {
        val pref = getSharedPreferences(DB_NAME, Context.MODE_PRIVATE)
        //Log.wtf("DATA", "data")
        return pref.getString(KEY, "").toString()
    }

    override fun onStop() {
        Log.wtf("onStop", "Stopped")
        //handle background task
        val tgTime = System.currentTimeMillis()+(60*1000)
        val intent = Intent(this, MyReceiver::class.java)
        val pi = PendingIntent.getBroadcast(this, REQ_CODE, intent, PendingIntent.FLAG_IMMUTABLE)
        alarmManager.set(AlarmManager.RTC_WAKEUP, tgTime, pi)
        super.onStop()
    }

}