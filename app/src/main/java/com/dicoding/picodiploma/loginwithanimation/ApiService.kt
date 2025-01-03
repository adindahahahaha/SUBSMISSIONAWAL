package com.dicoding.picodiploma.loginwithanimation

import android.telecom.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse
    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse
    @Headers("Content-Type: application/json")
    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String
    ): StoryResponse
    @GET("stories/{id}")
    suspend fun getStoryDetail(
        @Path("id") id: String
    ): StoryDetailResponse
}