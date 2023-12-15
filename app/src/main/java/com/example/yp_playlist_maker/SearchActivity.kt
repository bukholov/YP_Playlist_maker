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
    }
    private var searchTextToSave = ""
    private var lastSearchQuery = ""
    private val iTunesBaseURL = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(ITunesApi::class.java)
    private val trackList = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(trackList)
    
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun searchTracks(textTrack: String){
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
                        trackAdapter.notifyDataSetChanged()
                    }
                    else{
                        layoutInflater.inflate(R.layout.activity_not_found, flContent)
                    }
                }
            }
            override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                layoutInflater.inflate(R.layout.activity_no_internet, flContent)
                findViewById<LinearLayout>(R.id.ll_no_internet).findViewById<Button>(R.id.button_retry).setOnClickListener {
                    searchTracks(lastSearchQuery)
                }
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val buttonBackFromSearch= findViewById<TextView>(R.id.button_back_from_search)
        buttonBackFromSearch.setOnClickListener{
            finish()
        }
        val editTextSearch = findViewById<TextView>(R.id.edit_text_search)
        val clearButton = findViewById<ImageView>(R.id.image_view_clear_text)
        val textWatcherSearch = object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchTextToSave = findViewById<TextView>(R.id.edit_text_search).text.toString()
                clearButton.visibility = clearButtonVisibility(s)
            }
        }
        editTextSearch.addTextChangedListener(textWatcherSearch)

        val rvTracks = findViewById<RecyclerView>(R.id.rv_tracks)
        rvTracks.adapter = trackAdapter

        editTextSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                lastSearchQuery = textView.text.toString()
                searchTracks(textView.text.toString())
                true
            }
            false
        }

        clearButton.setOnClickListener {
            editTextSearch.setText("")
            val view: View? = this.currentFocus
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            if(view != null){
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
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