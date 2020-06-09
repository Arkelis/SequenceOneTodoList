package com.example.sequenceonetodolist.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataProvider {
    // private const val API_URL = "http://tomnab.fr/todo-api/"
    // private const val API_URL = "http://todo-api.localhost/api/"
    private const val API_URL = "http://192.168.1.52/api/"

    private val service = Retrofit.Builder()
        .baseUrl(API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TodoApiService::class.java)

    suspend fun login(login: String, password: String) = service.authenticate(login, password).hash
}