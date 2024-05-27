package com.example.yp_playlist_maker.player.domain.impl

import com.example.yp_playlist_maker.player.domain.db.FavoriteTracksInteractor
import com.example.yp_playlist_maker.player.domain.db.FavoriteTracksRepository
import com.example.yp_playlist_maker.search.domain.Track
import kotlinx.coroutines.flow.Flow

class LikedTracksInteractorImpl(private val likedTracksRepository: FavoriteTracksRepository): FavoriteTracksInteractor {
    override fun likedTracks(): Flow<List<Track>> {
        return likedTracksRepository.likedTracks()
    }
}