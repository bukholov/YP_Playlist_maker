package com.example.yp_playlist_maker

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


const val TRACK_KEY = "TRACK_KEY"
const val MAX_SAVED_TRACKS_HISTORY_SIZE = 10
class SearchHistory(private val sharedPreferences: SharedPreferences) {
    fun read(): ArrayList<Track> {
        Log.d("SearchHistory.read", "Read SharedPreferences")
        val json = sharedPreferences.getString(TRACK_KEY, null) ?: return ArrayList()
        Log.d("SearchHistory.read", "SharedPreferences is founded")

        val typeTokenTrack = object :TypeToken<ArrayList<Track>>() {}.type
        Log.d("SearchHistory.read", "Return ArrayList<Track> with saved tracks")
        return Gson().fromJson<ArrayList<Track>>(json, typeTokenTrack)
    }

    fun write(track: Track){
        Log.d("SearchHistory.write", "Read current saving history")
        val savedTracksArray = this.read()
        if(savedTracksArray.contains(track)){
            Log.d("SearchHistory.write", "Remove repetitions")
            savedTracksArray.remove(track)
        }
        //Если количество сохраняемых треков ВДРУГ изменится, то будут отображены только 10 последних добавленных треков
        while (savedTracksArray.size>= MAX_SAVED_TRACKS_HISTORY_SIZE){
            Log.d("SearchHistory.write", "Remove items exceeding size")
            savedTracksArray.removeFirst()
        }
        savedTracksArray.add(track)
        Log.d("SearchHistory.write", "Adding {%s} to SharedPreferences".format(track.toString()))
        sharedPreferences.edit()
            .putString(TRACK_KEY, Gson().toJson(savedTracksArray))
            .apply()
    }
}