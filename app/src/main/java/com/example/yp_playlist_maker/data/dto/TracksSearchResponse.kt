package com.example.yp_playlist_maker.data.dto

class TracksSearchResponse(val searchType: String,
                           val expression: String,
                           val results: List<TrackDto>): Response() {
}