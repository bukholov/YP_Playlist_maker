package com.example.yp_playlist_maker

class TracksResponse(val searchType: String,
                     val expression: String,
                     val results: List<Track>) {
}