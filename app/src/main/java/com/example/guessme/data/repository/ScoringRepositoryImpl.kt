package com.example.guessme.data.repository

import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import com.example.guessme.data.api.RetrofitApi
import com.example.guessme.data.response.ScoringResponseBody
import com.example.guessme.domain.repository.ScoringRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ScoringRepositoryImpl @Inject constructor(
    private val api: RetrofitApi
): ScoringRepository {
    override suspend fun quizScoring(
        token: String,
        image: String,
        infoValue: String,
        infoKey: String,
        personId: Int
    ): Response<ScoringResponseBody> {
        val imageFile = File(image)
        Log.d("filePath", imageFile.absolutePath)
        val imageRequestBody = imageFile.asRequestBody("image/jpeg".toMediaType())
        val imageMultipartBody = MultipartBody.Part.createFormData(name = "image", filename = imageFile.absolutePath, body = imageRequestBody)

        val personIdRequestBody = personId.toString().toRequestBody("text/plain".toMediaType())
        val infoKeyRequestBody = infoKey.toRequestBody("text/plain".toMediaType())
        val infoValueRequestBody = infoValue.toRequestBody("text/plain".toMediaType())

        return api.quizScoring(token, imageMultipartBody, infoValueRequestBody, infoKeyRequestBody, personIdRequestBody)
    }
}