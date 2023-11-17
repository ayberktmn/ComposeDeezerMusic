package com.ayberk.composedeezer.viewmodel

import android.media.MediaPlayer
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayberk.composedeezer.R
import com.ayberk.composedeezer.model.Genre.Data
import com.ayberk.composedeezer.model.artist.Artist
import com.ayberk.composedeezer.model.artistdetail.ArtistDetail
import com.ayberk.composedeezer.models.album.Album
import com.ayberk.composedeezer.retrofit.RetrofitRepository
import com.ayberk.composedeezer.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GenreViewModel @Inject constructor(
    private val repository: RetrofitRepository
) : ViewModel() {

    var genreList = mutableStateOf<List<Data>>(listOf())
    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> get() = _isPlaying

    private val _imageResourceId = MutableStateFlow(R.drawable.play)
    var imageResourceId: StateFlow<Int> get() = _imageResourceId
        set(value) {}

    private var mediaPlayer: MediaPlayer? = null

    init {
        loadGenre()
        _isPlaying.value = false
    }
    fun togglePlayPause(url: String) {
        if (_isPlaying.value) {
            stopPlayback()
        } else {
            startPlayback(url)
        }
    }

    private fun startPlayback(url: String) {
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(url)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
        _isPlaying.value = true
        _imageResourceId.value = R.drawable.pause
    }

    private fun stopPlayback() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        _isPlaying.value = false
        _imageResourceId.value = R.drawable.play
    }

    override fun onCleared() {
        super.onCleared()
        stopPlayback()
    }
    fun loadGenre() {
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.getGenre()

            when(result) {
                is Resource.Success -> {
                    val genreItems = result.data!!.data.mapIndexed { index, data ->
                        Data(data.id,data.name,data.picture,data.picture_big,data.picture_medium,data.picture_small,data.picture_xl,data.type)
                    } as List<Data>

                    errorMessage.value = ""
                    isLoading.value = false
                    genreList.value += genreItems
                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                    isLoading.value = false
                }
                is Resource.Loading -> {
                    errorMessage.value = ""
                }

                else -> {}
            }
        }
    }
    suspend fun LoadArtist(genre_id:Int): Resource<Artist> {
        return repository.getArtist(genre_id)
    }

    suspend fun LoadArtistDetail(artist_id:Int): Resource<ArtistDetail> {
        return repository.getArtistDetail(artist_id)
    }

    suspend fun LoadAlbum(artist_id:Int): Resource<Album> {
        return repository.getAlbum(artist_id)
    }

    suspend fun LoadMusic(album_id:Int): Resource<Album> {
        return repository.getMusic(album_id)
    }
}