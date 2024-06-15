package com.example.yp_playlist_maker.media.domain.db

import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    fun playlists(): Flow<List<Playlist>>

    suspend fun insertPlaylist(playlist: Playlist)

    suspend fun deletePlaylist(playlist: Playlist)

    suspend fun updatePlaylist(playlist: Playlist)
}