package com.example.yp_playlist_maker.data.network

import com.example.yp_playlist_maker.data.dto.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String) : Call<TracksSearchResponse>
}