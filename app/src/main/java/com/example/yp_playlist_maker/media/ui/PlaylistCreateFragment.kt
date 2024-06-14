package com.example.yp_playlist_maker.media.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.yp_playlist_maker.R
import com.example.yp_playlist_maker.databinding.FragmentPlaylistCreateBinding
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.viewmodel.PlaylistCreateViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class PlaylistCreateFragment : Fragment() {
    private var _binding: FragmentPlaylistCreateBinding? = null
    private val viewModelPlaylistCreate: PlaylistCreateViewModel by viewModel()
    private lateinit var textWatcher: TextWatcher
    companion object {
        private const val IS_SAVED_INSTANCE = "IS_SAVED_INSTANCE"
    }
    private val binding: FragmentPlaylistCreateBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistCreateBinding.inflate(layoutInflater)
        requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view).visibility = View.GONE
        val radiusRound = binding.root.resources.getDimension(R.dimen.search_corner_radius).roundToInt()

        textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.buttonCreatePlaylist.isEnabled = binding.editTextPlaylistName.editText?.text.toString().isNotBlank()
            }
        }
        textWatcher?.let { binding.editTextPlaylistName.editText?.addTextChangedListener(textWatcher) }

        if(savedInstanceState != null){
            if(savedInstanceState.getBoolean(IS_SAVED_INSTANCE)){
                val playlistFromSavedInstance = viewModelPlaylistCreate.getPlaylistFromSavedInstanceState()
                Log.d("onCreateView", "$playlistFromSavedInstance")
                binding.editTextPlaylistName.editText?.setText(playlistFromSavedInstance.playlistName)
                binding.editTextPlaylistDescription.editText?.setText(playlistFromSavedInstance.playlistDescription)
                binding.viewAddImage.tag = playlistFromSavedInstance.pathImage
                Glide.with(binding.root)
                    .load(playlistFromSavedInstance.pathImage)
                    .transform(CenterCrop(), RoundedCorners(radiusRound))
                    .into(binding.viewAddImage)
            }
        }

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(binding.root)
                        .load(uri)
                        .transform(CenterCrop(), RoundedCorners(radiusRound))
                        .into(binding.viewAddImage)
                    binding.viewAddImage.tag = uri.toString()
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        fun savePlaylist(){
            val playlist = Playlist(
                0,
                binding.editTextPlaylistName.editText?.text.toString(),
                binding.editTextPlaylistDescription.editText?.text.toString(),
                if(binding.viewAddImage.tag == null) "" else binding.viewAddImage.tag.toString())
            viewModelPlaylistCreate.createPlaylist(playlist)
            Toast.makeText(
                context,
                getString(R.string.playlist_created).format(playlist.playlistName),
                Toast.LENGTH_SHORT
            ).show()
            findNavController().popBackStack()
        }

        fun showMessageDialogClose(){
            if(binding.editTextPlaylistName.editText?.text?.isNotBlank() == true
                || binding.editTextPlaylistDescription.editText?.text?.isNotBlank() == true
                || (if(binding.viewAddImage.tag == null) "" else binding.viewAddImage.tag.toString()) != ""
            ) {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.finish_creating_playlist))
                    .setMessage(getString(R.string.unsaved_data_will_be_lost))
                    .setNeutralButton(getString(R.string.cancel)) { dialog, which ->
                    }
                    .setNegativeButton(getString(R.string.finish)) { dialog, which ->
                        findNavController().popBackStack()
                    }.show()
            }
            else{
                findNavController().popBackStack()
            }
        }

        binding.buttonBack.setOnClickListener {
            showMessageDialogClose()
        }

        binding.viewAddImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        binding.buttonCreatePlaylist.setOnClickListener {
            if(binding.editTextPlaylistName.editText?.text?.isNotBlank() == true) {
                savePlaylist()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                showMessageDialogClose()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModelPlaylistCreate.savePlaylistInstanceState(Playlist(
            0,
            binding.editTextPlaylistName.editText?.text.toString(),
            binding.editTextPlaylistDescription.editText?.text.toString(),
            if(binding.viewAddImage.tag == null) "" else binding.viewAddImage.tag.toString()))

        outState.putBoolean(IS_SAVED_INSTANCE, true)
    }
}