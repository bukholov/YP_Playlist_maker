package com.example.yp_playlist_maker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {
    companion object {
        const val SAVED_SEARCH_TEXT = "SAVED_SEARCH_TEXT"
    }
    private var searchTextToSave = ""
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
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

        clearButton.setOnClickListener {
            editTextSearch.setText("")
            val view: View? = this.currentFocus

            if(view != null){
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }

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
        val trackList = ArrayList<Track>(listOf(
            Track("Smells Like Teen Spirit", "Nirvana", "5:01", "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"),
            Track("Billie Jean", "Michael Jackson", "4:35", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
            Track("Stayin' Alive", "Bee Gees", "4:10", "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
            Track("Whole Lotta Love", "Led Zeppelin", "5:33", "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"),
            Track("Sweet Child O'Mine", "Guns N' Roses", "5:03", "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg"),
        ))
        val trackAdapter = TrackAdapter(trackList)
        val rvTracks = findViewById<RecyclerView>(R.id.rv_tracks)
        rvTracks.adapter = trackAdapter
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