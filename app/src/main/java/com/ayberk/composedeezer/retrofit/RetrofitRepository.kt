package com.ayberk.composedeezer.retrofit

import com.ayberk.composedeezer.model.Genre.Genre
import com.ayberk.composedeezer.model.artist.Artist
import com.ayberk.composedeezer.model.artistdetail.ArtistDetail
import com.ayberk.composedeezer.models.album.Album
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

    suspend fun getArtistDetail(artist_id:Int) : Resource<ArtistDetail>{
        val response = try {
            api.getArtistDetail(artist_id)
        } catch (e:Exception){
            return Resource.Error("ArtistDetail Error")
        }
        return Resource.Success(response)
    }

    suspend fun getAlbum(artist_id:Int) : Resource<Album>{
        val response = try {
            api.getAlbum(artist_id)
        } catch (e:Exception){
            return Resource.Error("Album Error")
        }
        return Resource.Success(response)
    }
}