package com.example.yp_playlist_maker.domain.api

import com.example.yp_playlist_maker.domain.models.Track

interface TracksRepository {
    fun searchTracks(expression: String): TracksConsumerData<ArrayList<Track>>
}