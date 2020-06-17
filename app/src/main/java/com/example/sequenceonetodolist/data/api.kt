package com.example.sequenceonetodolist.data

import com.example.sequenceonetodolist.model.*
import retrofit2.http.*

public interface TodoApiService {
    @FormUrlEncoded
    @POST("authenticate")
    suspend fun authenticate(@Field("user") user: String, @Field("password") password: String): AuthenticateResponse

    @GET("users")
    suspend fun users(@Query("hash") hash: String): UsersResponse

    @GET("lists")
    suspend fun lists(@Query("hash") hash: String): ListsResponse

    @FormUrlEncoded
    @POST("lists")
    suspend fun addList(@Field("label") name: String, @Query("hash") hash: String): ListCreatedResponse

    @GET("lists/{listId}/items")
    suspend fun items(@Path("listId") listId: Long, @Query("hash") hash: String): ItemsResponse

    @FormUrlEncoded
    @POST("lists/{listId}/items")
    suspend fun addItem(@Path("listId") listId: Long, @Field("label") itemName: String, @Query("hash") hash: String): ItemCreatedResponse

    @PUT("lists/{listId}/items/{itemId}")
    suspend fun toggleItem(@Path("listId") listId: Long, @Path("itemId") itemId: Long, @Query("check") check: Int, @Query("hash") hash: String): ItemToggleResponse
}