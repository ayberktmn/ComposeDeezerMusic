package com.ayberk.composedeezer.retrofit

import com.ayberk.composedeezer.model.Genre.Genre
import com.ayberk.composedeezer.model.artist.Artist
import com.ayberk.composedeezer.util.Resource
import javax.inject.Inject

class RetrofitRepository@Inject constructor(
    private val api: RetrofitInstance
) {
    suspend fun getGenre(): Resource<Genre> {
        val response = try {
            api.getGenre()
        } catch(e: Exception) {
            return Resource.Error("Genre Error")
        }
        return Resource.Success(response)
    }

    suspend fun getArtist(genre_Id:Int) : Resource<Artist>{
        val response = try {
            api.getArtist(genre_Id)
        } catch (e:Exception){
            return Resource.Error("Artist Error")
        }
        return Resource.Success(response)
    }

}