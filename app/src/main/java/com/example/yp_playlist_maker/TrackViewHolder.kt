package com.example.yp_playlist_maker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class TrackViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val tvTrackArtist: TextView = itemView.findViewById(R.id.text_view_track_artist)
    private val tvTrackName: TextView = itemView.findViewById(R.id.text_view_track_name)
    private val tvTrackTime: TextView = itemView.findViewById(R.id.text_view_track_time)
    private val ivTrackArt: ImageView = itemView.findViewById(R.id.image_view_track_art)
    private val rootLayout: LinearLayout = itemView.findViewById(R.id.rootLayout)

    @SuppressLint("ResourceType")
    fun bind(item: Track, sharedPreferences: SharedPreferences, context: Context){
        tvTrackArtist.text = item.artistName
        tvTrackName.text = item.trackName
        tvTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTime)

        val radiusRound = rootLayout.context.resources.getDimension(R.dimen.track_item_art_round_corner).roundToInt()

        Glide.with(rootLayout)
            .load(Uri.parse(item.artworkUrl100))
            .fitCenter()
            .apply(RequestOptions().placeholder(R.drawable.placeholder))
            .apply(RequestOptions.bitmapTransform(RoundedCorners(radiusRound)))
            .into(ivTrackArt)

        itemView.setOnClickListener {
            SearchHistory(sharedPreferences).write(item)

            val intent = Intent(context, AudioPlayerActivity::class.java)
            intent.putExtra("SELECTED_TRACK", Gson().toJson(item))
            context.startActivity(intent)
        }
    }
}