package com.example.yp_playlist_maker.player.domain

class PlayerInteractorImpl(private val playerRepository: PlayerRepository): PlayerInteractor {
    override fun getStartPosition(): String {
        return playerRepository.getStartPosition()
    }
}