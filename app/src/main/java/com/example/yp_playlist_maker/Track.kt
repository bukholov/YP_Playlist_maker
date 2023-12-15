package com.example.yp_playlist_maker

import com.google.gson.annotations.SerializedName

data class Track(val trackName: String,
                 @SerializedName("artistName") val artistName: String,
                 @SerializedName("trackTimeMillis") val trackTime: Int,
                 @SerializedName("artworkUrl100") val artworkUrl100: String) {
}