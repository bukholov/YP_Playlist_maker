package com.example.yp_playlist_maker.media.data.db.converters

import com.example.yp_playlist_maker.media.data.db.entity.TrackEntity
import com.example.yp_playlist_maker.search.domain.Track
import java.util.Date

class TrackDbConvertor {
    fun map(track: Track): TrackEntity{
        return TrackEntity(
            track.trackId,
            track.artworkUrl100,
            track.trackName,
            track.artistName,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.trackTime,
            track.previewUrl,
            Date(Date().time)
            )
    }

    fun map(track: TrackEntity): Track{
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.collectionName,
            track.primaryGenreName,
            track.country,
            track.releaseDate,
            track.previewUrl,
            true)
    }
}