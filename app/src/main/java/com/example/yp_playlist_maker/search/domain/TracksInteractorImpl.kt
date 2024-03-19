package com.example.yp_playlist_maker.search.domain

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.function.Consumer

class TracksInteractorImpl(private val repository: TracksRepository): TracksInteractor {
    override fun searchTracks(expression: String, consumer: Consumer<Result<List<Track>>>) {
        val executorService: ExecutorService = Executors.newFixedThreadPool(1)

        executorService.execute {
            consumer.accept(repository.searchTracks(expression))
        }
    }

    override fun loadTrackData(expression: String): Track {
        return repository.loadTrackData(expression)
    }


}