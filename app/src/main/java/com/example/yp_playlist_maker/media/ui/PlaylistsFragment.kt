package com.example.yp_playlist_maker.media.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.media.viewmodel.PlaylistsViewModel
import org.koin.android.ext.android.inject


class PlaylistsFragment : Fragment() {
    private val viewModelPlaylists: PlaylistsViewModel by inject<PlaylistsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_playlists, container, false)
    }

    companion object {
        fun newInstance() =
            PlaylistsFragment().apply {
            }
    }
}