package com.ayberk.composedeezer.retrofit

import com.ayberk.composedeezer.model.Genre.Genre
import com.ayberk.composedeezer.model.artist.Artist
import com.ayberk.composedeezer.model.artistdetail.ArtistDetail
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitInstance {
    @GET("genre")
    suspend fun getGenre(): Genre

    @GET("genre/{genre_id}/artists")
    suspend fun getArtist(@Path("genre_id") genre_id:Int): Artist

    @GET("artist/{artist_id}")
    suspend fun getArtistDetail(@Path("artist_id") artist_id:Int): ArtistDetail
}