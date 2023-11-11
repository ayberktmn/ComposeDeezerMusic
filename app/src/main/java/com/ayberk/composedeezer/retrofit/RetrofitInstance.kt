package com.ayberk.composedeezer.retrofit

import com.ayberk.composedeezer.model.Genre
import retrofit2.http.GET

interface RetrofitInstance { @GET("genre")
    suspend fun getGenre(): Genre
}