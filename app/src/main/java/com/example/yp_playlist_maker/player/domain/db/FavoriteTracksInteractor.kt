package com.example.yp_playlist_maker.player.domain.db

import com.example.yp_playlist_maker.search.domain.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTracksInteractor {
    fun likedTracks(): Flow<List<Track>>

    suspend fun likeTrack(track: Track)

    suspend fun unlikeTrack(track: Track)
}