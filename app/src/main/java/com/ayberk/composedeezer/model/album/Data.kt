package com.ayberk.composedeezer.model.album

import com.ayberk.composedeezer.models.album.AlbumX
import com.ayberk.composedeezer.models.album.Artist
import com.ayberk.composedeezer.models.album.Contributor


data class Data(


    val album: AlbumX,

    val artist: Artist,

    val contributors: List<Contributor>,

    val duration: Int,

    val explicit_content_cover: Int,

    val explicit_content_lyrics: Int,

    val explicit_lyrics: Boolean,

    val id: Long,

    val link: String,

    val md5_image: String,

    val preview: String,

    val rank: Int,

    val readable: Boolean,

    val title: String,

    val title_short: String,

    val title_version: String,

    val type: String
)