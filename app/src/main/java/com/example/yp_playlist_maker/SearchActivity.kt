package com.example.yp_playlist_maker

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
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {
    private companion object {
        const val SAVED_SEARCH_TEXT = "SAVED_SEARCH_TEXT"
        const val CONNECTION_SUCCESS = 200
        const val SAVED_TRACKS_PREFERENCES = "yp_playlist_saved_tracks"
        const val CLICK_DEBOUNCE_DELAY = 1000L
        const val SEARCH_DEBOUNCE_DELAY = 2000L
    }


    private var searchTextToSave = ""
    private var lastSearchQuery = ""
    private val iTunesBaseURL = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private var trackList = ArrayList<Track>()
    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

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

    private fun searchTracks(textTrack: String, trackAdapter: TrackAdapter){
        val flContent = findViewById<FrameLayout>(R.id.fl_content)
        val progressBarSearchTracks = findViewById<ProgressBar>(R.id.progress_bar_search_tracks)
        flContent.removeAllViewsInLayout()
        progressBarSearchTracks.visibility = View.VISIBLE
        trackList.clear()
        trackAdapter.notifyDataSetChanged()
        iTunesService.search(textTrack).enqueue(object : Callback<TracksResponse>{
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>
            ) {
                progressBarSearchTracks.visibility = View.GONE
                if(response.code() == CONNECTION_SUCCESS){
                    if(response.body()?.results?.isNotEmpty() == true){
                        trackList.addAll(response.body()?.results!!)
                        trackAdapter.notifyDataSetChanged()
                    }
                    else{
                        layoutInflater.inflate(R.layout.activity_not_found, flContent)
                    }
                }
            }
            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                progressBarSearchTracks.visibility = View.GONE
                layoutInflater.inflate(R.layout.activity_no_internet, flContent)
                flContent.visibility = View.VISIBLE
                findViewById<LinearLayout>(R.id.ll_no_internet).findViewById<Button>(R.id.button_retry).setOnClickListener {
                    searchTracks(lastSearchQuery, trackAdapter)
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val tracksSharedPreferences = getSharedPreferences(SAVED_TRACKS_PREFERENCES, MODE_PRIVATE)

        val onClickListener: (Track) -> Unit  = {item:Track ->
            if(clickDebounce()){
                Log.d("SearchActivity", "Open AudioPlayerActivity")
                SearchHistory(getSharedPreferences(SAVED_TRACKS_PREFERENCES, MODE_PRIVATE)).write(item)
                val intent = Intent(this, AudioPlayerActivity::class.java)
                intent.putExtra("SELECTED_TRACK", Gson().toJson(item))
                startActivity(intent)
            }
        }

        val trackAdapter = TrackAdapter(trackList, onClickListener)
        val buttonBackFromSearch= findViewById<TextView>(R.id.button_back_from_search)
        buttonBackFromSearch.setOnClickListener{
            finish()
        }
        val editTextSearch = findViewById<TextView>(R.id.edit_text_search)
        val buttonClear = findViewById<ImageView>(R.id.image_view_clear_text)
        val buttonClearHistory = findViewById<Button>(R.id.button_clear_history)
        val textViewLookingFor = findViewById<TextView>(R.id.tv_looking_for)
        buttonClearHistory.visibility = View.GONE


        val searchRunnable = Runnable {
                Log.d("SearchActivity", "Search query: {%s}".format(editTextSearch.text))
                buttonClearHistory.visibility = View.GONE
                textViewLookingFor.visibility = View.GONE
                searchTracks(editTextSearch.text.toString(), trackAdapter)
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
        val rvTracks = findViewById<RecyclerView>(R.id.rv_tracks)
        rvTracks.adapter = trackAdapter

        editTextSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                lastSearchQuery = textView.text.toString()
                searchTracks(textView.text.toString(), trackAdapter)
                buttonClearHistory.visibility = View.GONE
                textViewLookingFor.visibility = View.GONE
                true
            }
            false
        }

        editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus && editTextSearch.text.toString()==""){
                trackList.addAll(SearchHistory(tracksSharedPreferences).read().reversed())
                trackAdapter.notifyDataSetChanged()
                if(trackList.isNotEmpty()){
                    buttonClearHistory.visibility = View.VISIBLE
                    textViewLookingFor.visibility = View.VISIBLE
                }
            }
            else{
                buttonClearHistory.visibility = View.GONE
                textViewLookingFor.visibility = View.GONE
            }
        }

        buttonClear.setOnClickListener {
            editTextSearch.setText("")
            val view: View? = this.currentFocus
            trackList.clear()
            val savedTracksInHistory = SearchHistory(tracksSharedPreferences).read()
            savedTracksInHistory.reverse()
            trackList.addAll(savedTracksInHistory)
            trackAdapter.notifyDataSetChanged()
            buttonClearHistory.visibility = if (trackList.isNotEmpty()) View.VISIBLE else View.GONE
            textViewLookingFor.visibility = if (trackList.isNotEmpty()) View.VISIBLE else View.GONE
            findViewById<FrameLayout>(R.id.fl_content).visibility = View.GONE
            if(view != null){
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

        buttonClearHistory.setOnClickListener {
            tracksSharedPreferences.edit().
                clear().
                apply()
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            buttonClearHistory.visibility = View.GONE
            textViewLookingFor.visibility = View.GONE
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