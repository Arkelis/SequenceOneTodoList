package com.example.sequenceonetodolist.data

import com.example.sequenceonetodolist.model.AuthenticateResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

public interface TodoApiService {
    @FormUrlEncoded
    @POST("authenticate")
    suspend fun authenticate(@Field("user") user: String, @Field("password") password: String): AuthenticateResponse
}