package com.haypp.storyapp_reyjuna.api

import com.haypp.storyapp_reyjuna.data.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @POST("login")
    fun loginDO( @Body req: LoginRequest): Call<LoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun registerDO(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String): Call<RegisterResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody): Call<FileUploadResponse>

    @GET("stories")
    fun allStories(
        @Header("Authorization") token: String
    ): Call<AllStoriesResponse>
}