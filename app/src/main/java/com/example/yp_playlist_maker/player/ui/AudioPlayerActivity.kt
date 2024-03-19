package com.example.yp_playlist_maker.player.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.ActivityAudioPlayerBinding
import com.example.yp_playlist_maker.player.data.PlayerState
import com.example.yp_playlist_maker.player.view_model.AudioPlayerViewModel
import com.example.yp_playlist_maker.search.domain.Track
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.Locale
import kotlin.math.roundToInt

class AudioPlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var viewModel: AudioPlayerViewModel

    companion object{
        fun show(context: Context, track: Track){
            Log.d("SearchActivity", "Open AudioPlayerActivity")
            val intent = Intent(context, AudioPlayerActivity::class.java)
            intent.putExtra("SELECTED_TRACK", Gson().toJson(track))
            context.startActivity(intent)
        }
    }

    private fun pausePlayer() {
        binding.imageViewPlay.setImageResource(R.drawable.play)
    }

    private fun startPlayer(currentPosition: String) {
        binding.imageViewPlay.setImageResource(R.drawable.pause)
        binding.tvCurrentTrackTime.text = currentPosition
    }
    private fun render(playerState: PlayerState) {
        when(playerState){
            is PlayerState.StateDefault ->{
                binding.tvTrackName.text = playerState.track.trackName
                binding.tvArtistName.text = playerState.track.artistName
                binding.tvSelectedTrackDuration.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(playerState.track.trackTime)
                binding.tvSelectedTrackPrimaryGenreName.text = playerState.track.primaryGenreName
                binding.tvSelectedTrackCountry.text = playerState.track.country
                binding.tvSelectedTrackCollectionName.text = playerState.track.collectionName
                binding.tvSelectedTrackReleaseDate.text = SimpleDateFormat("yyyy", Locale.getDefault()).format(playerState.track.releaseDate)
                Glide.with(this)
                    .load(Uri.parse(playerState.track.getCoverArtwork()))
                    .fitCenter()
                    .placeholder(R.drawable.placeholder)
                    .apply(RequestOptions.bitmapTransform(RoundedCorners(this.resources.getDimension(R.dimen.audio_player_track_photo_round_corner).roundToInt())))
                    .into(binding.ivTrackPhoto)
            }
            is PlayerState.StatePlaying ->{
                startPlayer(playerState.currentPosition)
            }
            is PlayerState.Pause -> {
                pausePlayer()
            }
            is PlayerState.Prepared -> {
                pausePlayer()
            }
            is PlayerState.Complete -> {
                binding.imageViewPlay.setImageResource(R.drawable.play)
                binding.tvCurrentTrackTime.text = playerState.startPosition
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, AudioPlayerViewModel.getViewModelFactory())[AudioPlayerViewModel::class.java]
        viewModel.observeState().observe(this){
            render(it)
        }
        viewModel.loadTrack(stringExtra = intent.getStringExtra("SELECTED_TRACK")!!)

        binding.imageViewPlay.setOnClickListener {
            viewModel.playbackControl()
        }

        binding.buttonBackFromAudioPlayer.setOnClickListener {
            finish()
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.release()
    }
}
