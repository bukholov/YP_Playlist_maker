package com.example.yp_playlist_maker.search.view_model

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.yp_playlist_maker.player.ui.AudioPlayerActivity
import com.example.yp_playlist_maker.search.data.SearchState
import com.example.yp_playlist_maker.search.domain.Track
import com.example.yp_playlist_maker.search.domain.TracksHistoryInteractor
import com.example.yp_playlist_maker.search.domain.TracksInteractor
import org.koin.java.KoinJavaComponent.inject

class SearchViewModel: ViewModel() {
    companion object {
        const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }

    private var isClickAllowed = true
    private val handler: Handler by inject(Handler::class.java)
    private val tracksInteractor: TracksInteractor by inject(TracksInteractor::class.java)
    private val tracksHistoryInteractor: TracksHistoryInteractor by inject(TracksHistoryInteractor::class.java)
    private var detailsRunnable: Runnable? = null
    private var lastSearchQuery = ""
    private lateinit var searchRunnable: Runnable
    private val stateLiveData: MutableLiveData<SearchState> by inject(MutableLiveData::class.java)
    private val context: Context by inject(Context::class.java)

    fun observeState(): LiveData<SearchState> = stateLiveData

    fun searchDebounce(changeText: String) {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        searchRunnable = Runnable {
            Log.d("SearchActivity", "Search query: {%s}".format(changeText))
            searchTracks(changeText) }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            handler.postDelayed(
                searchRunnable,
                SEARCH_REQUEST_TOKEN,
                SEARCH_DEBOUNCE_DELAY_MILLIS
            )
        } else {
            val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY_MILLIS
            handler.postAtTime(
                searchRunnable,
                SEARCH_REQUEST_TOKEN,
                postTime,
            )
        }
    }

    fun searchTracks(textTrack: String) {
        renderState(SearchState.Loading)
        tracksInteractor.searchTracks(
            expression = textTrack
        ) { tracksConsumerData ->
            val currentRunnable = detailsRunnable
            if (currentRunnable != null) {
                handler.removeCallbacks(currentRunnable)
            }

            val newDetailsRunnable = Runnable {
                if (tracksConsumerData.isFailure) {
                    renderState(SearchState.Error{ searchTracks(this.lastSearchQuery) })
                } else if (tracksConsumerData.isSuccess) {
                    if (tracksConsumerData.getOrNull()?.isNotEmpty() == true) {
                        renderState(SearchState.Content(tracksConsumerData.getOrNull()!!))
                    } else {
                        renderState(
                            SearchState.Empty
                        )
                    }
                }
            }
            detailsRunnable = newDetailsRunnable
            handler.post(newDetailsRunnable)
        }
    }

    fun clickTrack(it: Track){
        if(clickDebounce()){
            tracksHistoryInteractor.write(it)
            if (stateLiveData.value is SearchState.History){
                showTracksHistory()
            }
            AudioPlayerActivity.show(context, it)
        }
    }

    fun showTracksHistory(){
        stateLiveData.postValue(SearchState.History(tracksHistoryInteractor.read().reversed()))
    }

    fun clearTracksHistory(){
        tracksHistoryInteractor.clearSavedTracks()
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY_MILLIS)
        }
        return current
    }
    private fun renderState(searchState: SearchState){
        stateLiveData.postValue(searchState)
    }

    override fun onCleared() {
        super.onCleared()
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }
}