package com.example.yp_playlist_maker.media.domain.db

import com.example.yp_playlist_maker.search.domain.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class Playlist(
    val playlistId: Int,
    val namePlaylistName: String,
    val descriptionPlaylist: String,
    val pathImage: String,
    var gsonTrackIdList: String)
{
    fun getTrackCountWord(): String{
        val countTracks = readTracks().count()

        if ((Math.abs(countTracks) % 100)>10 && (Math.abs(countTracks) % 100)< 20) {
            return "%d треков".format(countTracks)
        }
        else{
            return when(Math.abs(countTracks)%10){
                1 -> "%d трек".format(countTracks)
                2, 3, 4 -> "%d трека".format(countTracks)
                else -> "%d треков".format(countTracks)
            }
        }
    }

    fun readTracks(): List<Int>{
        if (gsonTrackIdList.isBlank())
            return ArrayList()
        val json = gsonTrackIdList?: return ArrayList()
        val typeTokenTrack = object : TypeToken<ArrayList<Int>>() {}.type

        return Gson().fromJson<List<Int>>(json, typeTokenTrack)
    }

    fun addToPlaylist(track: Track){
        val arrayListTracks = ArrayList<Int>()
        arrayListTracks.addAll(readTracks())
        arrayListTracks.add(track.trackId)

        gsonTrackIdList = Gson().toJson(arrayListTracks)
    }
}