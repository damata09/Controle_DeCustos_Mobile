package com.example.costcontrol.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    /**
     * false = emulador Android (10.0.2.2 aponta para o localhost do PC)
     * true  = celular físico na mesma rede Wi-Fi (use o IP do seu computador)
     */
    private const val USE_PHYSICAL_DEVICE = false

    // IP da sua rede local — altere se USE_PHYSICAL_DEVICE = true
    private const val PC_IP = "192.168.3.12"

    private val baseUrl: String = when {
        USE_PHYSICAL_DEVICE -> "http://$PC_IP:3000/"
        else -> "http://10.0.2.2:3000/"
    }

    val instance: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    /** URL em uso (útil para debug no Logcat) */
    fun currentBaseUrl(): String = baseUrl
}
