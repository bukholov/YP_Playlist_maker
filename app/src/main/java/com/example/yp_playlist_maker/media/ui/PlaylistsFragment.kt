package com.example.yp_playlist_maker.media.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.FragmentPlaylistsBinding
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.viewmodel.PlaylistsViewModel
import org.koin.android.ext.android.inject


class PlaylistsFragment : Fragment() {
    private lateinit var playlistAdapter: PlaylistAdapter
    private val viewModel: PlaylistsViewModel by inject<PlaylistsViewModel>()
    private lateinit var binding: FragmentPlaylistsBinding

    private fun render(result: Result<List<Playlist>>) {
        if (result.isSuccess) {
            if(result.getOrNull()!!.isEmpty()){
                with(playlistAdapter) {
                    playlists.clear()
                    notifyDataSetChanged()
                }
                binding.ivSadFace.visibility = View.VISIBLE
                binding.tvPlaylistsNotCreated.visibility = View.VISIBLE

            }
            else{
                binding.ivSadFace.visibility = View.GONE
                binding.tvPlaylistsNotCreated.visibility = View.GONE
                with(playlistAdapter) {
                    playlists.clear()
                    playlists.addAll(result.getOrNull()!!)
                    notifyDataSetChanged()
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)

        viewModel.observeState().observe(viewLifecycleOwner){
            render(it)
        }

        binding.buttonNewPlaylist.setOnClickListener {
            Log.d("PlaylistFragment", "Opening PlaylistCreateFragment")
            findNavController().navigate(R.id.playlistCreateFragment)
        }
        playlistAdapter = PlaylistAdapter()
        playlistAdapter.playlists = ArrayList()
        binding.rvPlaylists.adapter = playlistAdapter
        viewModel.fillData()

        return binding.root
    }

    companion object {
        fun newInstance() =
            PlaylistsFragment().apply {
            }
    }
}