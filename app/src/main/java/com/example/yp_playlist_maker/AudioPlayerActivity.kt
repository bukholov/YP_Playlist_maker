package com.example.yp_playlist_maker

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class AudioPlayerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        val selectedTrack = Gson().fromJson<Track>(intent.getStringExtra("SELECTED_TRACK"), object :TypeToken<Track>() {}.type)

        val tvTrackName = findViewById<TextView>(R.id.tv_track_name)
        val tvArtistName = findViewById<TextView>(R.id.tv_artist_name)
        val tvSelectedTrackDuration = findViewById<TextView>(R.id.tv_selected_track_duration)
        val tvSelectedTrackGenre = findViewById<TextView>(R.id.tv_selected_track_primary_genre_name)
        val tvSelectedTrackCountry = findViewById<TextView>(R.id.tv_selected_track_country)
        val tvSelectedTrackCollection = findViewById<TextView>(R.id.tv_selected_track_collection_name)
        val tvSelectedTrackYear = findViewById<TextView>(R.id.tv_selected_track_release_date)
        val ivTrackPhoto = findViewById<ImageView>(R.id.iv_track_photo)
        val buttonBackFromAudioPlayer = findViewById<Button>(R.id.button_back_from_audio_player)


        tvTrackName.text = selectedTrack.trackName
        tvArtistName.text = selectedTrack.artistName
        tvSelectedTrackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(selectedTrack.trackTime)

        tvSelectedTrackGenre.text = selectedTrack.primaryGenreName
        tvSelectedTrackCountry.text = selectedTrack.country
        tvSelectedTrackCollection.text = selectedTrack.collectionName
        tvSelectedTrackYear.text = SimpleDateFormat("yyyy", Locale.getDefault()).format(selectedTrack.releaseDate)

        Glide.with(this)
            .load(Uri.parse(selectedTrack.getCoverArtwork()))
            .fitCenter()
            .placeholder(R.drawable.placeholder)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(this.resources.getDimension(R.dimen.audio_player_track_photo_round_corner).roundToInt())))
            .into(ivTrackPhoto)

        buttonBackFromAudioPlayer.setOnClickListener {
            finish()
        }
    }
}