package com.example.yp_playlist_maker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yp_playlist_maker.media.data.db.entity.TracksInPlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksInPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTrack(tracksInPlaylist: TracksInPlaylistEntity)

    @Query("SELECT trackId FROM tracks_in_playlist_table WHERE playlistId = :playlistId")
    fun getTrackIdsInPlaylist(playlistId: Int): Flow<List<Int>>
}