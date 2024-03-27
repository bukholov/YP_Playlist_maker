package com.example.yp_playlist_maker.search.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.ActivitySearchBinding
import com.example.yp_playlist_maker.player.ui.AudioPlayerActivity
import com.example.yp_playlist_maker.search.data.SearchState
import com.example.yp_playlist_maker.search.domain.Track
import com.example.yp_playlist_maker.search.domain.TracksHistoryInteractor
import com.example.yp_playlist_maker.search.view_model.SearchViewModel
import org.koin.android.ext.android.inject

class SearchActivity : AppCompatActivity() {
    private lateinit var trackAdapter: TrackAdapter
    private val tracksHistoryInteractor: TracksHistoryInteractor by inject()
    private val viewModel: SearchViewModel by inject()
    private lateinit var textWatcher: TextWatcher
    private lateinit var binding: ActivitySearchBinding
    private var isVisibleHistoryFeatures : Boolean = false

    private fun showHistoryFeatures(isVisible: Boolean){
        isVisibleHistoryFeatures = isVisible
        if(isVisible){
            binding.buttonClearHistory.visibility = View.VISIBLE
            binding.tvLookingFor.visibility = View.VISIBLE
        }
        else{
            binding.buttonClearHistory.visibility = View.GONE
            binding.tvLookingFor.visibility = View.GONE
        }
    }

    private fun showLoading() {
        binding.flContent.removeAllViewsInLayout()
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
        showHistoryFeatures(false)
        binding.progressBarSearchTracks.visibility = View.VISIBLE
    }

    private fun showError(onClickListener: OnClickListener){
        binding.progressBarSearchTracks.visibility = View.GONE
        layoutInflater.inflate(R.layout.activity_no_internet, binding.flContent)
        findViewById<LinearLayout>(R.id.ll_no_internet).findViewById<Button>(R.id.button_retry).setOnClickListener(onClickListener)
    }

    private fun showEmpty(){
        binding.progressBarSearchTracks.visibility = View.GONE
        binding.flContent.removeAllViewsInLayout()
        layoutInflater.inflate(R.layout.activity_not_found, binding.flContent)
    }

    private fun showContent(newTrackList: List<Track>){
        binding.progressBarSearchTracks.visibility = View.GONE
        with(trackAdapter) {
            tracks.clear()
            tracks.addAll(newTrackList)
            notifyDataSetChanged()
        }
    }

    private fun render(state: SearchState) {
        when(state){
            is SearchState.Content -> showContent(state.tracks)
            is SearchState.Error -> showError(state.onClickListener)
            is SearchState.Loading -> showLoading()
            is SearchState.Empty -> showEmpty()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonClearHistory.visibility = View.GONE

        val click = {it: Track->
            tracksHistoryInteractor.write(it)
            AudioPlayerActivity.show(this, it)
            if (isVisibleHistoryFeatures) {
                showContent(tracksHistoryInteractor.read().reversed())
            }
        }
        trackAdapter = TrackAdapter {
            viewModel.clickTrack(click(it))
        }
        trackAdapter.tracks = ArrayList()

        binding.rvTracks.adapter = trackAdapter

        viewModel.observeState().observe(this){
            render(it)
        }

        binding.buttonBackFromSearch.setOnClickListener {
            finish()
        }

        fun clearButtonVisibility(s: CharSequence?): Int {
            return if (s.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(s.toString())
                binding.imageViewClearText.visibility = clearButtonVisibility(s)
            }
        }
        textWatcher?.let { binding.editTextSearch.addTextChangedListener(textWatcher) }

        binding.editTextSearch.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_DONE){
                viewModel.searchTracks(textView.text.toString())
                true
            }
            false
        }
        binding.editTextSearch.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus && binding.editTextSearch.text.toString()==""){
                trackAdapter.tracks.addAll(tracksHistoryInteractor.read().reversed())
                trackAdapter.notifyDataSetChanged()
                if(trackAdapter.tracks.isNotEmpty()){
                    showHistoryFeatures(true)
                }
            }
            else{
                showHistoryFeatures(false)
            }

        }
        binding.imageViewClearText.setOnClickListener {
            binding.editTextSearch.setText("")
            val view: View? = this.currentFocus
            showContent(tracksHistoryInteractor.read())
            showHistoryFeatures(trackAdapter.tracks.isNotEmpty())
            binding.flContent.visibility = View.GONE

            if(view != null){
                val inputMethodManager = this.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            }
        }
        binding.buttonClearHistory.setOnClickListener {
            tracksHistoryInteractor.clearSavedTracks()
            trackAdapter.tracks.clear()
            trackAdapter.notifyDataSetChanged()
            showHistoryFeatures(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { binding.editTextSearch.removeTextChangedListener(it) }
    }
}