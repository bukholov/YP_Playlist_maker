package com.example.yp_playlist_maker.media.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yp_playlist_maker.media.domain.db.Playlist
import com.example.yp_playlist_maker.media.domain.db.PlaylistRepository
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent
import java.io.File
import java.io.FileOutputStream

class PlaylistCreateViewModel(private val playlistRepository: PlaylistRepository): ViewModel() {
    private val context: Context by KoinJavaComponent.inject(Context::class.java)
    fun createPlaylist(playlist: Playlist){
        viewModelScope.launch {
            playlistRepository.insertPlaylist(playlist)
        }
    }

    fun saveImageToPrivateStorage(uri: Uri) {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), uri.toString())

        if (!filePath.exists()){
            filePath.mkdirs()
        }

        val file = File(filePath, "example.jpg")
        val inputStream = context?.contentResolver?.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
    }
}