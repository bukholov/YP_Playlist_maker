package com.example.yp_playlist_maker.media.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.ActivityMediaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMediaBinding
    private lateinit var tabMedia: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pagerMedia.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMedia = TabLayoutMediator(binding.tabLayoutMedia, binding.pagerMedia){ tab, position ->
            when(position){
                0 -> tab.text = getString(R.string.favorite_tracks)
                1 -> tab.text = getString(R.string.playlists)
            }
        }
        tabMedia.attach()

        binding.buttonBackFromMedia.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMedia.detach()
    }
}