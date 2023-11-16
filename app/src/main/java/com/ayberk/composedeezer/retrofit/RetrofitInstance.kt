package com.ayberk.composedeezer.retrofit

import com.ayberk.composedeezer.model.Genre.Genre
import com.ayberk.composedeezer.model.artist.Artist
import com.ayberk.composedeezer.model.artistdetail.ArtistDetail
import com.ayberk.composedeezer.models.album.Album
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitInstance {
    @GET("genre")
    suspend fun getGenre(): Genre

    @GET("genre/{genre_id}/artists")
    suspend fun getArtist(@Path("genre_id") genre_id:Int): Artist

    @GET("artist/{artist_id}")
    suspend fun getArtistDetail(@Path("artist_id") artist_id:Int): ArtistDetail

    @GET("artist/{artist_id}/top?limit=50")
    suspend fun getAlbum(@Path("artist_id") artist_id: Int) : Album

}