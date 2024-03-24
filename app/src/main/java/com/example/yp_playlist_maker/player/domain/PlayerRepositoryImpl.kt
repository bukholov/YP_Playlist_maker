package com.example.yp_playlist_maker.player.domain

import android.content.Context
import com.example.yp_playlist_maker.R

class PlayerRepositoryImpl(private val context: Context):PlayerRepository {
    override fun getStartPosition(): String {
        return context.getString(R.string.audio_player_start_position)
    }
}