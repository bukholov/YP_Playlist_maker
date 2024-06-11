package com.example.yp_playlist_maker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.yp_playlist_maker.media.data.db.converters.Converters
import com.example.yp_playlist_maker.media.data.db.dao.PlaylistDao
import com.example.yp_playlist_maker.media.data.db.dao.TrackDao
import com.example.yp_playlist_maker.media.data.db.entity.PlaylistEntity
import com.example.yp_playlist_maker.media.data.db.entity.TrackEntity


@Database(version = 1, entities = [TrackEntity::class, PlaylistEntity::class])
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){
    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao
}