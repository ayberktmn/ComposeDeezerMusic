package com.ayberk.composedeezer.retrofit

import com.ayberk.composedeezer.model.Genre
import com.ayberk.composedeezer.util.Resource
import javax.inject.Inject

class RetrofitRepository@Inject constructor(
    private val api: RetrofitInstance
) {

    suspend fun getGenre(): Resource<Genre> {
        val response = try {
            api.getGenre()
        } catch(e: Exception) {
            return Resource.Error("Error")
        }
        return Resource.Success(response)
    }

}