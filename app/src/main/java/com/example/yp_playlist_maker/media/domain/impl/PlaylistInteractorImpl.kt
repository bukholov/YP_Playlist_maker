package com.example.yp_playlist_maker.media.domain.impl

import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.domain.db.PlaylistInteractor
import com.example.yp_playlist_maker.media.domain.db.PlaylistRepository
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val playlistRepository: PlaylistRepository): PlaylistInteractor {
    override fun playlists(): Flow<List<Playlist>> {
        return playlistRepository.playlists()
    }

    override suspend fun syncPlaylist(playlist: Playlist) {
        return playlistRepository.updatePlaylist(playlist)
    }
}