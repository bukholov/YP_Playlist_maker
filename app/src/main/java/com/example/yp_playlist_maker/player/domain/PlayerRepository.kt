package com.example.yp_playlist_maker.player.domain

interface PlayerRepository {
    fun getStartPosition(): String
}