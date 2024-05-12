package com.example.yp_playlist_maker.player.view_model

import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.player.data.PlayerState
import com.example.yp_playlist_maker.player.domain.PlayerInteractor
import com.example.yp_playlist_maker.search.domain.TracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel(private val tracksInteractor: TracksInteractor,
                           private val playerInteractor: PlayerInteractor
): ViewModel() {
    companion object {
        const val SHOW_TIME_DEBOUNCE_DELAY_MILLIS = 300L
    }
    private val mediaPlayer: MediaPlayer by inject(MediaPlayer::class.java)
    private val stateLiveData: MutableLiveData<PlayerState> by inject(MutableLiveData::class.java)
    private var timerJob: Job? = null
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
            createUpdatePositionTask()
        }
    }

    fun pausePlayer(){
        if(mediaPlayer.isPlaying){
            mediaPlayer.pause()
            timerJob?.cancel()
            renderState(PlayerState.Pause)
        }
    }

    private fun renderState(playerState: PlayerState) {
        stateLiveData.postValue(playerState)
    }

    private fun createUpdatePositionTask(){
        timerJob = viewModelScope.launch{
            Log.d("Position task", "Load current position: %s".format(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)))
            while(mediaPlayer.isPlaying){
                renderState(PlayerState.StatePlaying(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)))
                delay(SHOW_TIME_DEBOUNCE_DELAY_MILLIS)
            }
        }
    }
}