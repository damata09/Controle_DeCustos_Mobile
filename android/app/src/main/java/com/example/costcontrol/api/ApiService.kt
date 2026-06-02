package com.example.costcontrol.api

import com.example.costcontrol.model.Category
import com.example.costcontrol.model.Cost
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    // --- CATEGORIAS ---
    @GET("categorias")
    fun getCategories(): Call<List<Category>>

    @GET("categorias/{id}")
    fun getCategoryById(@Path("id") id: Int): Call<Category>

    @POST("categorias")
    fun createCategory(@Body category: Category): Call<Category>

    @PUT("categorias/{id}")
    fun updateCategory(@Path("id") id: Int, @Body category: Category): Call<Category>

    @DELETE("categorias/{id}")
    fun deleteCategory(@Path("id") id: Int): Call<ResponseBody>


    // --- CUSTOS ---
    @GET("custos")
    fun getCosts(): Call<List<Cost>>

    @GET("custos/{id}")
    fun getCostById(@Path("id") id: Int): Call<Cost>

    @POST("custos")
    fun createCost(@Body cost: Cost): Call<Cost>

    @PUT("custos/{id}")
    fun updateCost(@Path("id") id: Int, @Body cost: Cost): Call<Cost>

    @DELETE("custos/{id}")
    fun deleteCost(@Path("id") id: Int): Call<ResponseBody>
}
