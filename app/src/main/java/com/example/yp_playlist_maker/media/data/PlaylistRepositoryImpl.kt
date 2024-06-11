package com.example.yp_playlist_maker.media.data

import com.example.yp_playlist_maker.media.data.db.AppDatabase
import com.example.yp_playlist_maker.media.data.db.converters.PlaylistDbConvertor
import com.example.yp_playlist_maker.media.data.db.entity.PlaylistEntity
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.domain.db.PlaylistRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor
): PlaylistRepository {
    override fun playlists(): Flow<List<Playlist>> = flow{
        appDatabase.playlistDao().getPlaylists().collect{
            emit(convertFromPlaylistEntity(it))
        }
    }

    override suspend fun insertPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(playlistDbConvertor.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().deletePlaylist(playlistDbConvertor.map(playlist))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDatabase.playlistDao().updatePlaylist(playlistDbConvertor.map(playlist))
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>):List<Playlist> {
        return playlists.map { playlistEntity -> playlistDbConvertor.map(playlistEntity) }
    }
}