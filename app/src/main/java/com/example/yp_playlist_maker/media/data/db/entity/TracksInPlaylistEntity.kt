package com.example.yp_playlist_maker.media.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_in_playlist_table")
data class TracksInPlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val tracksInPlaylistId: Int,
    val playlistId: Int,
    val trackId: Int
)