package com.example.cars.proxy.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CarRetrofit {

    companion object{
        fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/renting-car/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}