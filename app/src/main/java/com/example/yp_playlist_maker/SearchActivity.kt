package com.example.yp_playlist_maker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
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

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchTracks(textTrack: String, trackAdapter: TrackAdapter){
        val flContent = findViewById<FrameLayout>(R.id.fl_content)
        flContent.removeAllViewsInLayout()
        iTunesService.search(textTrack).enqueue(object : Callback<TracksResponse>{
            override fun onResponse(
                call: Call<TracksResponse>,
                response: Response<TracksResponse>
            ) {
                if(response.code() == CONNECTION_SUCCESS){
                    trackList.clear()
                    if(response.body()?.results?.isNotEmpty() == true){
                        trackList.addAll(response.body()?.results!!)
                    }
                    else{
                        layoutInflater.inflate(R.layout.activity_not_found, flContent)
                    }
                    trackAdapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                trackList.clear()
                trackAdapter.notifyDataSetChanged()
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
        val trackAdapter = TrackAdapter(trackList, tracksSharedPreferences, this)
        val buttonBackFromSearch= findViewById<TextView>(R.id.button_back_from_search)
        buttonBackFromSearch.setOnClickListener{
            finish()
        }
        val editTextSearch = findViewById<TextView>(R.id.edit_text_search)
        val buttonClear = findViewById<ImageView>(R.id.image_view_clear_text)
        val buttonClearHistory = findViewById<Button>(R.id.button_clear_history)
        val textViewLookingFor = findViewById<TextView>(R.id.tv_looking_for)
        buttonClearHistory.visibility = View.GONE

        val textWatcherSearch = object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
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