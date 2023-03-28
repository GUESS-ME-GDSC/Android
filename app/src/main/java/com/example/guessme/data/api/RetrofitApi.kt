package com.example.guessme.data.api

import com.example.guessme.data.model.InfoList
import com.example.guessme.data.model.User
import com.example.guessme.data.response.BaseResponseBody
import com.example.guessme.data.response.PersonResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface RetrofitApi {
    @POST("/auth/login")
    suspend fun logIn(
        @Body user: User
    ): Response<BaseResponseBody>

    @POST("/auth/join")
    suspend fun signUp(
        @Body user: User
    ): Response<BaseResponseBody>

    @Multipart
    @POST("/person/")
    suspend fun addPerson(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part?,
        @Part voice: MultipartBody.Part?,
        @Part("name") name: RequestBody,
        @Part("relation") relation: RequestBody,
        @Part("birth") birth: RequestBody,
        @Part("residence") residence: RequestBody
    ): Response<BaseResponseBody>

    @POST("/person/{id}/newinfo")
    suspend fun addInfo(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
        @Body info: InfoList
    ): Response<BaseResponseBody>

    @GET("/person/{id}")
    suspend fun getPerson(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): Response<PersonResponse>
}