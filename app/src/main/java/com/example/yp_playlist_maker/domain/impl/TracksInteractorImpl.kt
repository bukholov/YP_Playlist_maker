package com.example.yp_playlist_maker.domain.impl

import com.example.yp_playlist_maker.domain.api.TracksInteractor
import com.example.yp_playlist_maker.domain.api.TracksRepository

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {

        val t = Thread {
            consumer.consume(repository.searchTracks(expression))
          }
        t.start()
    }


}