package com.ayberk.composedeezer.models.album




data class Album(
    val `data`: List<com.ayberk.composedeezer.model.album.Data>,
    val next: String,
    val total: Int
)