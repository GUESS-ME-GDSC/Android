package com.example.guessme.data.api

import com.example.guessme.data.model.InfoList
import com.example.guessme.data.model.NewScore
import com.example.guessme.data.model.User
import com.example.guessme.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

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
    ): Response<BaseNullResponseBody>

    @POST("/person/{id}/newinfo")
    suspend fun addInfo(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int,
        @Body info: InfoList
    ): Response<BaseNullResponseBody>

    @GET("/person/{id}")
    suspend fun getPerson(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): Response<PersonResponse>

    @GET("/person/all")
    suspend fun getPeopleList(
        @Header("Authorization") authorization: String,
        @Query("favorite") favorite: Boolean
    ): Response<PeopleListResponseBody>

    @GET("/quiz/create/{id}")
    suspend fun getPersonQuiz(
        @Header("Authorization") authorization: String,
        @Path("id") id: Int
    ): Response<QuizResponseBody>

    @Multipart
    @POST("/quiz/scoring")
    suspend fun quizScoring(
        @Header("Authorization") authorization: String,
        @Part image: MultipartBody.Part,
        @Part("infoValue") infoValue: RequestBody
    ): Response<ScoringResponseBody>

    @PATCH("/quiz/newscore")
    suspend fun patchNewScore(
        @Header("Authorization") authorization: String,
        @Body newScore: NewScore
    ): Response<BaseResponseBody>

}