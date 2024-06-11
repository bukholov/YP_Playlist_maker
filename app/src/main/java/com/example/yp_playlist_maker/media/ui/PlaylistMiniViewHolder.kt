package com.example.yp_playlist_maker.media.ui

import android.annotation.SuppressLint
import android.net.Uri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.PlaylistItemMiniBinding
import com.example.yp_playlist_maker.media.domain.db.Playlist
import kotlin.math.roundToInt

class PlaylistMiniViewHolder(
    private val binding: PlaylistItemMiniBinding,
    onClickListener: (position: Int) -> Unit
): RecyclerView.ViewHolder(binding.root) {

    init {
        itemView.setOnClickListener {
            onClickListener(adapterPosition)
        }
    }
    @SuppressLint("ResourceType")
    fun bind(item: Playlist){
        binding.textViewPlaylistName.text = item.namePlaylistName
        binding.textViewPlaylistTrackCount.text = item.getTrackCountWord()

        val radiusRound = binding.root.resources.getDimension(R.dimen.track_item_art_round_corner).roundToInt()

        Glide.with(binding.root)
            .load(Uri.parse(item.pathImage))
            .apply(RequestOptions().placeholder(R.drawable.placeholder))
            .transform(CenterCrop(), RoundedCorners(radiusRound))
            .into(binding.imageViewPlaylist)
    }
}