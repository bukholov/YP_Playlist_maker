package com.example.yp_playlist_maker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.yp_playlist_maker.media.data.db.entity.TrackEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTrack(track: TrackEntity)

    @Delete
    suspend fun deleteTrack(track: TrackEntity)

    @Query("SELECT * FROM track_table")
    fun getTracks(): Flow<List<TrackEntity>>
}