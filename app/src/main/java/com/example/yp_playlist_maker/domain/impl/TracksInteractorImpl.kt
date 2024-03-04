package com.example.yp_playlist_maker.domain.impl

import com.example.yp_playlist_maker.domain.api.TracksInteractor
import com.example.yp_playlist_maker.domain.api.TracksRepository
import com.example.yp_playlist_maker.domain.models.Track
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


}