package com.example.application.server

import com.example.application.data.model.Service
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object ApiClientExercises {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://90.156.224.51:8000")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val service: Service = retrofit.create(Service::class.java)
}