package com.example.yp_playlist_maker.media.data

import com.example.yp_playlist_maker.media.data.db.AppDatabase
import com.example.yp_playlist_maker.media.data.db.entity.TracksInPlaylistEntity
import com.example.yp_playlist_maker.media.domain.db.TracksInPlaylistRepository
import kotlinx.coroutines.flow.Flow

class TracksInPlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
): TracksInPlaylistRepository {
    override suspend fun insertTrackInPlaylist(playlistId: Int, trackId: Int) {
        appDatabase.tracksInPlaylistDao().insertTrack(TracksInPlaylistEntity(0, playlistId, trackId))
    }

    override fun getTrackIdsInPlaylist(playlistId: Int): Flow<List<Int>> {
        return appDatabase.tracksInPlaylistDao().getTrackIdsInPlaylist(playlistId)
    }
}