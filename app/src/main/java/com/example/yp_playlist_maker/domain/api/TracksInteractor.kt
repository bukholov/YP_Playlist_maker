package com.example.yp_playlist_maker.domain.api

import com.example.yp_playlist_maker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)
    interface TracksConsumer{
        fun consume(foundData: TracksConsumerData<ArrayList<Track>>)
    }
}