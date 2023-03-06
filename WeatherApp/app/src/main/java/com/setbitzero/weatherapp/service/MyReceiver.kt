package com.setbitzero.weatherapp.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReceiver:BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val pref = context?.getSharedPreferences("CITY_NAME", Context.MODE_PRIVATE)
        val editor = pref?.edit()
        editor?.clear()
        editor?.putString("CityName", "Dhaka")
        editor?.apply()

    }
}