package com.example.yp_playlist_maker.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.domain.Creator
import com.example.yp_playlist_maker.domain.models.Track
import com.example.yp_playlist_maker.presentation.ui.tracks.TrackAdapter
import com.google.gson.Gson

class SearchActivity : AppCompatActivity() {
    private companion object {
        const val SAVED_SEARCH_TEXT = "SAVED_SEARCH_TEXT"

        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    private var searchTextToSave = ""
    private var lastSearchQuery = ""
    private var trackList = ArrayList<Track>()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private var detailsRunnable: Runnable? = null
    private val tracksInteractor = Creator.provideTracksInteractor()

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val tracksHistoryInteractor = Creator.provideTracksHistoryInteractor(context = applicationContext)
        val onClickListener: (Track) -> Unit  = { item: Track ->
            if(clickDebounce()){
                Log.d("SearchActivity", "Open AudioPlayerActivity")
                tracksHistoryInteractor.write(item)
                val intent = Intent(this, AudioPlayerActivity::class.java)
                intent.putExtra("SELECTED_TRACK", Gson().toJson(item))
                startActivity(intent)
            }
        }

        val trackAdapter = TrackAdapter(trackList, onClickListener)
        val rvTracks = findViewById<RecyclerView>(R.id.rv_tracks)
        rvTracks.adapter = trackAdapter
        val buttonBackFromSearch= findViewById<TextView>(R.id.button_back_from_search)
        buttonBackFromSearch.setOnClickListener{
            finish()
        }
        val editTextSearch = findViewById<TextView>(R.id.edit_text_search)
        val buttonClear = findViewById<ImageView>(R.id.image_view_clear_text)
        val buttonClearHistory = findViewById<Button>(R.id.button_clear_history)
        val textViewLookingFor = findViewById<TextView>(R.id.tv_looking_for)
        val progressBarSearchTracks = findViewById<ProgressBar>(R.id.progress_bar_search_tracks)
        val flContent = findViewById<FrameLayout>(R.id.fl_content)
        buttonClearHistory.visibility = View.GONE

        fun hideHistoryFeatures(){
            buttonClearHistory.visibility = View.GONE
            textViewLookingFor.visibility = View.GONE
        }

        fun showHistoryFeatures(){
            buttonClearHistory.visibility = View.VISIBLE
            textViewLookingFor.visibility = View.VISIBLE
        }

        fun searchTracks(textTrack: String) {
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            flContent.removeAllViewsInLayout()
            hideHistoryFeatures()
            progressBarSearchTracks.visibility = View.VISIBLE

            tracksInteractor.searchTracks(
                expression = textTrack
            ) { tracksConsumerData ->
                val currentRunnable = detailsRunnable
                if (currentRunnable != null) {
                    handler.removeCallbacks(currentRunnable)
                }

                val newDetailsRunnable = Runnable {
                    progressBarSearchTracks.visibility = View.GONE

                    if (tracksConsumerData.isFailure) {
                        layoutInflater.inflate(R.layout.activity_no_internet, flContent)
                        findViewById<LinearLayout>(R.id.ll_no_internet).findViewById<Button>(R.id.button_retry)
                            .setOnClickListener {
                                searchTracks(lastSearchQuery)
                            }
                    } else if (tracksConsumerData.isSuccess) {
                        if (tracksConsumerData.getOrNull()?.isNotEmpty() == true) {
                            trackList.addAll(tracksConsumerData.getOrNull()!!)
                            trackAdapter.notifyDataSetChanged()
                        } else {
                            layoutInflater.inflate(R.layout.activity_not_found, flContent)
                        }
                    }
                }
                detailsRunnable = newDetailsRunnable
                handler.post(newDetailsRunnable)
            }
        }

        val searchRunnable = Runnable {
                Log.d("SearchActivity", "Search query: {%s}".format(editTextSearch.text))
                searchTracks(editTextSearch.text.toString())
        }

        fun searchDebounce() {
            handler.removeCallbacks(searchRunnable)
            handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
        }

        val textWatcherSearch = object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                searchTextToSave = findViewById<TextView>(R.id.edit_text_search).text.toString()
                buttonClear.visibility = clearButtonVisibility(s)
            }
        }
        editTextSearch.addTextChangedListener(textWatcherSearch)


        editTextSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                lastSearchQuery = textView.text.toString()
                searchTracks(textView.text.toString())
                true
            }
            false
        }

        editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus && editTextSearch.text.toString()==""){
                trackList.addAll(tracksHistoryInteractor.read().reversed())
                trackAdapter.notifyDataSetChanged()
                if(trackList.isNotEmpty()){
                    showHistoryFeatures()
                }
            }
            else{
                hideHistoryFeatures()
            }
        }

        buttonClear.setOnClickListener {
            editTextSearch.setText("")
            val view: View? = this.currentFocus
            trackList.clear()
            val savedTracksInHistory = tracksHistoryInteractor.read()
            trackList.addAll(savedTracksInHistory.reversed())
            trackAdapter.notifyDataSetChanged()

            if (trackList.isNotEmpty()){
                showHistoryFeatures()
            }
            else{
                hideHistoryFeatures()
            }

            findViewById<FrameLayout>(R.id.fl_content).visibility = View.GONE

            if(view != null){
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        buttonClearHistory.setOnClickListener {
            tracksHistoryInteractor.clearSavedTracks()
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            hideHistoryFeatures()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVED_SEARCH_TEXT, searchTextToSave)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle)
    {
        super.onRestoreInstanceState(savedInstanceState)
        searchTextToSave = savedInstanceState.getString(SAVED_SEARCH_TEXT, "")
        findViewById<TextView>(R.id.edit_text_search).text = searchTextToSave
    }
}