package com.example.yp_playlist_maker.player.ui.view_model

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.domain.db.PlaylistInteractor
import com.example.yp_playlist_maker.player.data.PlayerState
import com.example.yp_playlist_maker.player.data.PlaylistsState
import com.example.yp_playlist_maker.player.domain.PlayerInteractor
import com.example.yp_playlist_maker.player.domain.db.FavoriteTracksInteractor
import com.example.yp_playlist_maker.search.domain.Track
import com.example.yp_playlist_maker.search.domain.TracksInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerViewModel( private val tracksInteractor: TracksInteractor,
                            private val playerInteractor: PlayerInteractor,
                            private val favoriteTracksInteractor: FavoriteTracksInteractor,
                            private val playlistInteractor: PlaylistInteractor
): ViewModel() {
    companion object {
        const val SHOW_TIME_DEBOUNCE_DELAY_MILLIS = 300L
    }
    private val mediaPlayer: MediaPlayer by inject(MediaPlayer::class.java)
    private val stateLiveData: MutableLiveData<PlayerState> by inject(MutableLiveData::class.java)
    private val playlistLiveData: MutableLiveData<PlaylistsState> by inject(MutableLiveData::class.java)
    private var timerJob: Job? = null
    private var track: Track? = null
    private val context: Context by inject(Context::class.java)
    private var alreadyPrepared = true

    fun observeState(): LiveData<PlayerState> = stateLiveData

    fun observePlaylistState(): LiveData<PlaylistsState> = playlistLiveData

    fun loadTrack(stringExtra: String){
        track = tracksInteractor.loadTrackData(stringExtra)

        viewModelScope.launch {
            favoriteTracksInteractor
                .likedTracks()
                .collect{tracks ->
                    tracks.map {
                        if(track!!.trackId == it.trackId){
                            track!!.isFavorite = it.isFavorite
                        }
                    }
                }
        }
        if(alreadyPrepared) {
            with(mediaPlayer) {
                mediaPlayer.setDataSource(track!!.previewUrl)
                mediaPlayer.prepareAsync()
                setOnPreparedListener {
                    renderState(PlayerState.Prepared)
                }
                setOnCompletionListener {
                    playerInteractor.getStartPosition()
                    renderState(PlayerState.Complete(playerInteractor.getStartPosition()))
                }
                alreadyPrepared = false
                renderState(PlayerState.StateDefault(track!!))
            }
        }
        else{
            renderState(PlayerState.Resume(track!!, SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)))
        }
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

    private fun renderPlaylists(playlistState: PlaylistsState) {
        playlistLiveData.postValue(playlistState)
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

    fun likeTrack(){
        viewModelScope.launch {
            if(track!!.isFavorite){
                Log.d("Favorite track", "delete from favorite")
                favoriteTracksInteractor.unlikeTrack(track!!)
            }
            else{
                Log.d("Favorite track", "add to favorite")
                favoriteTracksInteractor.likeTrack(track!!)
            }
            track!!.isFavorite = !track!!.isFavorite
            renderState(PlayerState.StateDefault(track!!))
        }
    }

    fun addToPlaylist(playlist: Playlist){
        Log.d("AudioPlayerViewModel", "addToPlaylist ${track}")
        viewModelScope.launch {
            if(playlist.readTracks().contains(track!!.trackId)){
                renderPlaylists(PlaylistsState.StateMakeMessage(context.getString(R.string.track_already_added_to_playlist).format(playlist.namePlaylistName)))
            }
            else{
                playlist.addToPlaylist(track!!)
                playlistInteractor.syncPlaylist(playlist)
                renderPlaylists(PlaylistsState.StateMakeMessage(context.getString(R.string.added_to_playlist).format(playlist.namePlaylistName)))
                playlistInteractor
                    .playlists()
                    .collect{playlists->
                        renderPlaylists(PlaylistsState.StateData(playlists))
                    }
            }
        }
    }

    fun showPlaylists(){
        viewModelScope.launch {
            playlistInteractor
                .playlists()
                .collect{playlists->
                    renderPlaylists(PlaylistsState.StateData(playlists))
                }
        }
    }
}