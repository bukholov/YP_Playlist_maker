package com.example.yp_playlist_maker.search.repository


import com.example.yp_playlist_maker.search.data.NetworkClient
import com.example.yp_playlist_maker.search.data.dto.TracksSearchRequest
import com.example.yp_playlist_maker.search.data.dto.TracksSearchResponse
import com.example.yp_playlist_maker.search.domain.Track
import com.example.yp_playlist_maker.search.domain.TracksRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

const val CONNECTION_SUCCESS = 200
class TracksRepositoryImpl(private val networkClient: NetworkClient): TracksRepository {
    override fun searchTracks(expression: String): Result<ArrayList<Track>> {
        val response = networkClient.doRequest(TracksSearchRequest(expression))

        return if (response.resultCode == CONNECTION_SUCCESS){
            Result.success(ArrayList((response as TracksSearchResponse).results.map {
                Track(it.trackId,
                    it.trackName,
                    it.artistName,
                    it.trackTime,
                    it.artworkUrl100,
                    it.collectionName,
                    it.primaryGenreName,
                    it.country,
                    it.releaseDate,
                    it.previewUrl)
            }
            ))
        }
        else {
            Result.failure(exception = Throwable())
        }
    }

    override fun loadTrackData(expression: String): Track {
        return Gson().fromJson<Track>(expression, object : TypeToken<Track>() {}.type)
    }
}
