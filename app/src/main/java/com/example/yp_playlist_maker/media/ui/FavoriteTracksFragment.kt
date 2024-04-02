package com.example.yp_playlist_maker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.media.viewmodel.FavoriteTracksViewModel
import org.koin.android.ext.android.inject

class FavoriteTracksFragment : Fragment() {
    private val viewModelFavoriteTracks: FavoriteTracksViewModel by inject<FavoriteTracksViewModel>()
    companion object {
        fun newInstance() =
            FavoriteTracksFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_favorite_tracks, container, false)
    }
}