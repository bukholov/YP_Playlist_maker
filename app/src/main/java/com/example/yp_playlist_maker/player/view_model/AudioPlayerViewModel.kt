package com.example.yp_playlist_maker.player.view_model

import android.app.Application
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.yp_playlist_maker.creator.Creator
import com.example.yp_playlist_maker.player.data.PlayerState
import com.example.yp_playlist_maker.player.domain.PlayerInteractor
import com.example.yp_playlist_maker.search.domain.TracksInteractor
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(private val tracksInteractor: TracksInteractor,
                           private val playerInteractor: PlayerInteractor
): ViewModel() {
    companion object {
        const val SHOW_TIME_DEBOUNCE_DELAY_MILLIS = 500L
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myApp = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                val tracksInteractor = Creator.provideTracksInteractor()
                val playerInteractor = Creator.providePlayerRepository(myApp)
                AudioPlayerViewModel(tracksInteractor, playerInteractor)
            }
        }
    }
    private var mediaPlayer = MediaPlayer()
    private val stateLiveData = MutableLiveData<PlayerState>()
    private val handler = Handler(Looper.getMainLooper())
    fun observeState(): LiveData<PlayerState> = stateLiveData

    fun loadTrack(stringExtra: String){
        val track = tracksInteractor.loadTrackData(stringExtra)

        with(mediaPlayer){
            mediaPlayer.setDataSource(track.previewUrl)
            mediaPlayer.prepareAsync()
            setOnPreparedListener {
                renderState(PlayerState.Prepared)
            }
            setOnCompletionListener {
                playerInteractor.getStartPosition()
                renderState(PlayerState.Complete(playerInteractor.getStartPosition()))
            }
        }
        renderState(PlayerState.StateDefault(track))
    }

    fun release(){
        mediaPlayer.release()
    }

    fun playbackControl(){
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
            renderState(PlayerState.Pause)
        }
        else{
            mediaPlayer.start()
            handler.post(
                createUpdatePositionTask()
            )
        }
    }

    fun pausePlayer(){
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
            renderState(PlayerState.Pause)
        }
    }

    private fun renderState(playerState: PlayerState) {
        stateLiveData.postValue(playerState)
    }

    private fun createUpdatePositionTask():Runnable{
        return object : Runnable{
            override fun run() {
                Log.d("Position task", "Load current position: %s".format(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)))
                if(mediaPlayer.isPlaying){
                    renderState(PlayerState.StatePlaying(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)))
                    handler.postDelayed(this, SHOW_TIME_DEBOUNCE_DELAY_MILLIS)
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SHOW_TIME_DEBOUNCE_DELAY_MILLIS)
    }
}