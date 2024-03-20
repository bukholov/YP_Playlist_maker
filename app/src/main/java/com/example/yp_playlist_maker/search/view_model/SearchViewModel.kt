package com.example.yp_playlist_maker.search.view_model

import android.app.Application
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.creator.Creator
import com.example.yp_playlist_maker.search.data.SearchState

class SearchViewModel(private val application: Application): ViewModel() {
    companion object {
        const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val myApp = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                SearchViewModel(myApp)
            }
        }
    }


    private var isClickAllowed = true

    private val handler = Handler(Looper.getMainLooper())
    private val tracksInteractor = Creator.provideTracksInteractor()
    private var detailsRunnable: Runnable? = null
    private var lastSearchQuery = ""
    private lateinit var searchRunnable: Runnable

    private val stateLiveData = MutableLiveData<SearchState>()
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
                            SearchState.Empty(
                                message = application.getString(R.string.not_found)
                            )
                        )
                    }
                }
            }
            detailsRunnable = newDetailsRunnable
            handler.post(newDetailsRunnable)
        }
    }

    fun clickTrack(unit: Unit){
        if(clickDebounce()){
            unit
        }
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