package com.example.yp_playlist_maker.media.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.yp_playlist_maker.databinding.PlaylistItemMiniBinding
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.domain.db.TracksInPlaylistInteractor
import org.koin.java.KoinJavaComponent

class PlaylistMiniAdapter(val onClickListener: (Playlist)->Unit) : RecyclerView.Adapter<PlaylistMiniViewHolder> () {
    lateinit var playlists: ArrayList<Playlist>
    private val tracksInPlaylistInteractor: TracksInPlaylistInteractor by KoinJavaComponent.inject(
        TracksInPlaylistInteractor::class.java
    )
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistMiniViewHolder {
        val binding = PlaylistItemMiniBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return PlaylistMiniViewHolder(binding, tracksInPlaylistInteractor){position->
            playlists.getOrNull(position)?.let { playlist->
                onClickListener(playlist)
            }
        }
    }

    override fun onBindViewHolder(holder: PlaylistMiniViewHolder, position: Int) {
        playlists.getOrNull(position)?.let {
            holder.bind(playlists[position])
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }
}