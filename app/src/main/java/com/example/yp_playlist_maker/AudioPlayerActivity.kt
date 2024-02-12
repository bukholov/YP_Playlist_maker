package com.example.yp_playlist_maker

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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

    private companion object {
        const val SHOW_TIME_DEBOUNCE_DELAY = 500L
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private lateinit var imageViewPlay: ImageView
    private lateinit var tvCurrentTrackTime: TextView
    private lateinit var selectedTrack: Track
    private val handler = Handler(Looper.getMainLooper())

    private fun preparePlayer() {
        with(mediaPlayer){
            setDataSource(selectedTrack.previewUrl)
            prepareAsync()
            setOnPreparedListener {
                playerState = STATE_PREPARED
            }
            setOnCompletionListener {
                playerState = STATE_PREPARED
                imageViewPlay.setImageResource(R.drawable.play)
                tvCurrentTrackTime.text = getString(R.string.audio_player_start_position)
            }
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        imageViewPlay.setImageResource(R.drawable.pause)
        playerState = STATE_PLAYING
        handler.post(
            createUpdatePositionTask()
        )
    }

    private fun pausePlayer() {
        if(mediaPlayer.isPlaying)
            mediaPlayer.pause()
        imageViewPlay.setImageResource(R.drawable.play)
        playerState = STATE_PAUSED
    }

    private fun playbackControl(inputPlayerState: Int) {
        when(inputPlayerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun createUpdatePositionTask():Runnable{
        return object : Runnable{
            override fun run() {
                Log.d("Position task", "Load current position: %s".format(SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)))
                if(playerState == STATE_PLAYING){
                    tvCurrentTrackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
                    handler.postDelayed(this, SHOW_TIME_DEBOUNCE_DELAY)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        selectedTrack = Gson().fromJson<Track>(intent.getStringExtra("SELECTED_TRACK"), object :TypeToken<Track>() {}.type)

        val tvTrackName = findViewById<TextView>(R.id.tv_track_name)
        val tvArtistName = findViewById<TextView>(R.id.tv_artist_name)
        val tvSelectedTrackDuration = findViewById<TextView>(R.id.tv_selected_track_duration)
        val tvSelectedTrackGenre = findViewById<TextView>(R.id.tv_selected_track_primary_genre_name)
        val tvSelectedTrackCountry = findViewById<TextView>(R.id.tv_selected_track_country)
        val tvSelectedTrackCollection = findViewById<TextView>(R.id.tv_selected_track_collection_name)
        val tvSelectedTrackYear = findViewById<TextView>(R.id.tv_selected_track_release_date)
        val ivTrackPhoto = findViewById<ImageView>(R.id.iv_track_photo)
        val buttonBackFromAudioPlayer = findViewById<Button>(R.id.button_back_from_audio_player)
        imageViewPlay = findViewById<ImageView>(R.id.image_view_play)
        tvCurrentTrackTime = findViewById<TextView>(R.id.tv_current_track_time)

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
        preparePlayer()
        imageViewPlay.setOnClickListener {
            playbackControl(playerState)
        }
    }

    override fun onPause() {
        super.onPause()
        pausePlayer()
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
    }
