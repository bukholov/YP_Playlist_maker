package com.example.yp_playlist_maker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.internal.ViewUtils

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

        val buttonBackFromSearch= findViewById<TextView>(R.id.buttonBackFromSearch)
        buttonBackFromSearch.setOnClickListener{
            finish()
        }
        val editTextSearch = findViewById<TextView>(R.id.editTextSearch)
        val clearButton = findViewById<ImageView>(R.id.imageViewClearText)

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
                clearButton.visibility = clearButtonVisibility(s)
            }
        }
        editTextSearch.addTextChangedListener(textWatcherSearch)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        searchTextToSave = findViewById<TextView>(R.id.editTextSearch).text.toString()
        outState.putString(SAVED_SEARCH_TEXT, searchTextToSave)
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle)
    {
        super.onRestoreInstanceState(savedInstanceState)
        searchTextToSave = savedInstanceState.getString(SAVED_SEARCH_TEXT, "")
        findViewById<TextView>(R.id.editTextSearch).text = searchTextToSave
    }
}