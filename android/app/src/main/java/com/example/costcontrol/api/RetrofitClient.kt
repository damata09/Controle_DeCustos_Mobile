package com.example.costcontrol.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // URL base padrão para o emulador Android (10.0.2.2 mapeia para o localhost do computador host)
    // Se utilizar um dispositivo físico, altere para o IP local do computador (ex: http://192.168.x.x:3000/)
    private const val BASE_URL = "http://10.0.2.2:3000/"

    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}
