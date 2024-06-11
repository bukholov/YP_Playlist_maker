package com.example.yp_playlist_maker.media.ui

import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.koin.android.ext.android.inject
import kotlin.math.roundToInt

class PlaylistCreateFragment : Fragment() {
    private var _binding: FragmentPlaylistCreateBinding? = null
    private val viewModel: PlaylistCreateViewModel by inject<PlaylistCreateViewModel>()
    private lateinit var textWatcher: TextWatcher
    private var uriImage: String = ""

    private val binding: FragmentPlaylistCreateBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistCreateBinding.inflate(layoutInflater)

        val radiusRound = binding.root.resources.getDimension(R.dimen.search_corner_radius).roundToInt()

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) {
                    Glide.with(binding.root)
                        .load(uri)
                        .transform(CenterCrop(), RoundedCorners(radiusRound))
                        .into(binding.viewAddImage)
                    uriImage = uri.toString()
                } else {
                    Log.d("PhotoPicker", "No media selected")
                }
            }

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.viewAddImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

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


        binding.buttonCreatePlaylist.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle(getString(R.string.finish_creating_playlist))
                .setMessage(getString(R.string.unsaved_data_will_be_lost))
                .setNeutralButton(getString(R.string.cancel)) { dialog, which ->

                }
                .setNegativeButton(getString(R.string.finish)) { dialog, which ->
                    findNavController().popBackStack()
                }
                .setPositiveButton(getString(R.string.create)) { dialog, which ->
                    if(uriImage.isNotBlank()){
                        viewModel.saveImageToPrivateStorage(Uri.parse(uriImage))
                    }

                    val playlist = Playlist(
                        0,
                        binding.editTextPlaylistName.editText?.text.toString(),
                        binding.editTextPlaylistDescription.editText?.text.toString(),
                        uriImage,
                        ""
                    )
                    viewModel.createPlaylist(playlist)
                    Toast.makeText(context, getString(R.string.playlist_created).format(playlist.namePlaylistName), Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                .show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}