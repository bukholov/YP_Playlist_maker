package com.example.yp_playlist_maker.media.domain.db

data class Playlist(
    val playlistId: Int,
    val playlistName: String,
    val playlistDescription: String,
    val pathImage: String)