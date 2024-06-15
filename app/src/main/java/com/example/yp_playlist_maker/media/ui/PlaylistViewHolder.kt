package com.example.yp_playlist_maker.media.ui

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.PlaylistItemBinding
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.domain.db.TracksInPlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.io.File
import kotlin.math.roundToInt

class PlaylistViewHolder(
    private val binding: PlaylistItemBinding,
    private val tracksInPlaylistInteractor: TracksInPlaylistInteractor
): RecyclerView.ViewHolder(binding.root) {
    private val context: Context by KoinJavaComponent.inject(Context::class.java)
    @SuppressLint("ResourceType")
    fun bind(item: Playlist){
        binding.textViewPlaylistName.text = item.playlistName
        GlobalScope.launch(Dispatchers.Main) {
            tracksInPlaylistInteractor.getTrackIdsInPlaylist(item.playlistId).collect{
                Log.d("PlaylistMiniViewHolder", context.resources.getQuantityString(R.plurals.count_of_track_numbers, it.size, it.size))
                binding.textViewPlaylistTrackCount.text = context.resources.getQuantityString(R.plurals.count_of_track_numbers, it.size, it.size)
            }
        }

        val radiusRound = binding.root.resources.getDimension(R.dimen.playlist_create_button_create_round_corners).roundToInt()

        Glide.with(binding.root)
            .load(File(item.pathImage, ""))
            .apply(RequestOptions().placeholder(R.drawable.placeholder))
            .transform(CenterCrop(), RoundedCorners(radiusRound))
            .into(binding.imageViewPlaylist)
    }
}
