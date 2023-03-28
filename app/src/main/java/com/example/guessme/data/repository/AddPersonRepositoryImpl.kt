package com.example.guessme.data.repository

import android.util.Log
import com.example.guessme.data.api.RetrofitApi
import com.example.guessme.data.model.Person
import com.example.guessme.data.response.AddPersonRequestBody
import com.example.guessme.domain.repository.AddPersonRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AddPersonRepositoryImpl @Inject constructor(
    private val api: RetrofitApi
): AddPersonRepository {

    override suspend fun addPerson(token: String, person: Person, image: File?): Response<AddPersonRequestBody> {
        Log.d("person", person.toString())
        var imageRequestBody: RequestBody?
        var imageMultipartBody: MultipartBody.Part? = null
        image?.let {
            imageRequestBody = image.asRequestBody("image/jpeg".toMediaType())
            imageMultipartBody = MultipartBody.Part.createFormData(name= "image", filename = image.name, body = imageRequestBody!!)
        }

        var voiceFile: File?
        var voiceRequestBody: RequestBody?
        var voiceMultipartBody: MultipartBody.Part? = null
        person.voice?.let {
            voiceFile = File(it.path!!)
            voiceRequestBody = voiceFile!!.asRequestBody("audio/mp4".toMediaType())
            voiceMultipartBody = MultipartBody.Part.createFormData(name= "image", filename = voiceFile!!.name, body = voiceRequestBody!!)
        }

        val nameRequestBody = person.name.toRequestBody("text/plain".toMediaType())
        val relationRequestBody = person.relation.toRequestBody("text/plain".toMediaType())
        val birthRequestBody = person.birth.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val residenceRequestBody = person.residence.toRequestBody("text/plain".toMediaType())

        return api.addPerson(
            "Bearer $token",
            imageMultipartBody,
            voiceMultipartBody,
            nameRequestBody,
            relationRequestBody,
            birthRequestBody,
            residenceRequestBody
        )
    }
}