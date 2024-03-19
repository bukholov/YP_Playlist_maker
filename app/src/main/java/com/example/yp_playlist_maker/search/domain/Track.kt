package com.example.yp_playlist_maker.search.domain

import java.util.Date

data class Track(val trackId: Int,
                 val trackName: String,
                 val artistName: String,
                 val trackTime: Int,
                 val artworkUrl100: String,
                 val collectionName: String,
                 val primaryGenreName: String,
                 val country: String,
                 val releaseDate: Date,
                 val previewUrl: String

    ) {
    fun getCoverArtwork() = artworkUrl100.replaceAfterLast('/',"512x512bb.jpg")
}