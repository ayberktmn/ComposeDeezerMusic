package com.ayberk.composedeezer.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayberk.composedeezer.model.Genre.Data
import com.ayberk.composedeezer.model.artist.Artist
import com.ayberk.composedeezer.retrofit.RetrofitRepository
import com.ayberk.composedeezer.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GenreViewModel @Inject constructor(
    private val repository: RetrofitRepository
) : ViewModel() {

    var genreList = mutableStateOf<List<Data>>(listOf())
    var artisList = mutableStateOf<List<com.ayberk.composedeezer.model.artist.Data>>(listOf())
    var errorMessage = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    init {
        loadGenre()
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
}