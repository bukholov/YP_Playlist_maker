package com.example.yp_playlist_maker.media.data.db.converters

import com.example.yp_playlist_maker.media.data.db.entity.TrackEntity
import com.example.yp_playlist_maker.search.domain.Track

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
            track.previewUrl)
    }

    fun map(track: TrackEntity): Track{
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTime,
            track.artworkUrl100,
            track.primaryGenreName,
            track.country,
            track.collectionName,
            track.releaseDate,
            track.previewUrl,
            true)
    }
}