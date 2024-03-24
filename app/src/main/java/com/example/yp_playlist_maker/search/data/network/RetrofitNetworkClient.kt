package com.example.yp_playlist_maker.search.data.network

import android.util.Log
import com.example.yp_playlist_maker.search.data.NetworkClient
import com.example.yp_playlist_maker.search.data.dto.Response
import com.example.yp_playlist_maker.search.data.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient: NetworkClient {

    private val iTunesBaseURL = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)
    override fun doRequest(dto: Any): Response {
        return try{
            if (dto is TracksSearchRequest) {
                val resp = iTunesService.search(dto.expression).execute()
                val body = resp.body() ?: Response()

                body.apply { resultCode = resp.code() }
            } else {
                Response().apply { resultCode = 400 }
            }
        } catch (e: Exception){
            Log.d("ERROR", "400")
            Response().apply { resultCode = 400 }
        }

    }

}